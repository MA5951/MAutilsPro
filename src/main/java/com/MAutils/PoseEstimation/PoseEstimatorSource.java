package com.MAutils.PoseEstimation;

import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Supplier;

import com.MAutils.Utils.Constants;

import edu.wpi.first.math.geometry.Twist2d;

public class PoseEstimatorSource {
  private static final class Measurement {
    final Twist2d twist;
    final double fomXY, fomTheta;
    final double timestamp;

    Measurement(Twist2d t, double fXY, double fTh, double ts) {
      twist = t;
      fomXY = fXY;
      fomTheta = fTh;
      timestamp = ts;
    }
  }

  // Buffer caps (choose generous margins vs. estimator window & worst-case latency)
  private static final double MAX_AGE_SEC = 0.5;
  private static final int MAX_SAMPLES = (int) (MAX_AGE_SEC / 0.02);
   // keep >= HISTORY_WINDOW_SEC + latency margin

  private final Twist2d blankTwist = new Twist2d();

  // Chronological storage by timestamp
  private final NavigableMap<Double, Measurement> buffer = new TreeMap<>();

  private final Supplier<Twist2d> twistSupplier;
  private final Supplier<Double> fomXYSupplier;
  private final Supplier<Double> fomThetaSupplier;
  private final Supplier<Double> timestampSupplier;

  public PoseEstimatorSource(
      Supplier<Twist2d> twistSupplier,
      Supplier<Double> fomXYSupplier,
      Supplier<Double> fomThetaSupplier,
      Supplier<Double> timestampSupplier) {

    this.twistSupplier = twistSupplier;
    this.fomXYSupplier = fomXYSupplier;
    this.fomThetaSupplier = fomThetaSupplier;
    this.timestampSupplier = timestampSupplier;

    PoseEstimator.addSource(this);
  }

  /** Legacy name kept for drop-in compatibility. */
  public void sendMeausrment() { // NOPMD - preserve original API
    sendMeasurement();
  }

  /** Push the current suppliers as a timestamped sample. */
  public void sendMeasurement() {
    double ts = timestampSupplier.get();
    double fxy = sanitizeFom(fomXYSupplier.get());
    double fth = sanitizeFom(fomThetaSupplier.get());

    Measurement m = new Measurement(twistSupplier.get(), fxy, fth, ts);
    buffer.put(ts, m);
    prune();
  }

  /** Did we record anything earlier than t (which would require a replay)? */
  public boolean hasBefore(double t) {
    return !buffer.isEmpty() && buffer.firstKey() < t;
  }

  /** Step-sample at time t (latest sample with timestamp <= t). */
  public Twist2d getTwistAt(double t) {
    var e = buffer.floorEntry(t);
    return (e != null) ? e.getValue().twist : blankTwist;
  }

  public double getFomXYAt(double t) {
    var e = buffer.floorEntry(t);
    return (e != null) ? e.getValue().fomXY : Constants.EPSILON;
  }

  public double getFomThetaAt(double t) {
    var e = buffer.floorEntry(t);
    return (e != null) ? e.getValue().fomTheta : Constants.EPSILON;
  }


  // ---- helpers ----

  private static double sanitizeFom(Double v) {
    if (v == null) return Constants.EPSILON;
    return (v <= 0) ? Constants.EPSILON : v;
  }

  /** Prevent unbounded growth and keep a tight time window. */
  private void prune() {
    // size cap
    while (buffer.size() > MAX_SAMPLES) {
      buffer.pollFirstEntry();
    }
    // age cap relative to newest
    var newest = buffer.isEmpty() ? null : buffer.lastEntry();
    if (newest != null) {
      double minAllowed = newest.getKey() - MAX_AGE_SEC;
      while (!buffer.isEmpty() && buffer.firstKey() < minAllowed) {
        buffer.pollFirstEntry();
      }
    }
  }
}
