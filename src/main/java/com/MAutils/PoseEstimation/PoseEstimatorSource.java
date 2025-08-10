package com.MAutils.PoseEstimation;

import java.util.*;
import java.util.function.Supplier;

import com.MAutils.Utils.Constants;

import edu.wpi.first.math.geometry.Twist2d;

public class PoseEstimatorSource {
    private static class Measurment {
        final Twist2d twist;
        final double fomXY, fomTheta, timestamp;

        Measurment(Twist2d t, double fXY, double fTh, double ts) {
            twist = t;
            fomXY = fXY;
            fomTheta = fTh;
            timestamp = ts;
        }
    }

    private final Twist2d blankTwist = new Twist2d();
    private final List<Measurment> buffer = new ArrayList<>(); 
    private final Supplier<Twist2d> twistSupplier;
    private final Supplier<Double> fomXYSupplier;
    private final Supplier<Double> fomThetaSupplier;
    private final Supplier<Double> timestampSupplier;
    private double fomXY;
    private double fomTheta;
    private int idx;
    private Measurment newMeasurment;

    public PoseEstimatorSource(Supplier<Twist2d> twistSupplier,
                               Supplier<Double> fomXYSupplier,
                               Supplier<Double> fomThetaSupplier,
                               Supplier<Double> timestampSupplier) {
        this.twistSupplier = twistSupplier;
        this.fomXYSupplier = fomXYSupplier;
        this.fomThetaSupplier = fomThetaSupplier;
        this.timestampSupplier = timestampSupplier;
        PoseEstimator.addSource(this);
    }

    public void sendMeausrment() {
        fomXY = fomXYSupplier.get() <= 0 ? Constants.EPSILON : fomXYSupplier.get();
        fomTheta = fomThetaSupplier.get() <= 0 ? Constants.EPSILON : fomThetaSupplier.get();
        newMeasurment = new Measurment(twistSupplier.get(), fomXY, fomTheta, timestampSupplier.get());
        idx = Collections.binarySearch(buffer, newMeasurment,
                Comparator.comparingDouble(xm -> xm.timestamp));
        if (idx < 0) {
            idx = -idx - 1;
        }
        buffer.add(idx, newMeasurment);
    }

    public boolean hasBefore(double t) {
        return !buffer.isEmpty() && buffer.get(0).timestamp < t;
    }

    public Twist2d getTwistAt(double t) {
        for (int i = buffer.size() - 1; i >= 0; --i) {
            if (buffer.get(i).timestamp <= t) {
                return buffer.get(i).twist;
            }
        }
        return blankTwist;
    }

    public double getFomXYAt(double t) {
        for (int i = buffer.size() - 1; i >= 0; --i) {
            if (buffer.get(i).timestamp <= t) {
                return buffer.get(i).fomXY;
            }
        }
        return Constants.EPSILON;
    }

    public double getFomThetaAt(double t) {
        for (int i = buffer.size() - 1; i >= 0; --i) {
            if (buffer.get(i).timestamp <= t) {
                return buffer.get(i).fomTheta;
            }
        }
        return Constants.EPSILON;
    }
}
