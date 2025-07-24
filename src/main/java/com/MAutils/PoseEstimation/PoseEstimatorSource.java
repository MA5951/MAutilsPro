package com.MAutils.PoseEstimation;

import java.util.*;

import com.MAutils.Utils.Constants;

import edu.wpi.first.math.geometry.Twist2d;

public class PoseEstimatorSource {
    private static class Measurment {
        final Twist2d twist;
        final double fom, timestamp;
        Measurment(Twist2d t, double f, double ts) {
            twist = t; fom = f; timestamp = ts;
        }
    }

    private final List<Measurment> buffer = new ArrayList<>();

    public PoseEstimatorSource() {
    }

    public synchronized void addMeasurement(Twist2d delta, double fom, double timestamp) {
        if (fom <= 0) fom = Constants.EPSILON;
        Measurment p = new Measurment(delta, fom, timestamp);
        int idx = Collections.binarySearch(buffer, p, Comparator.comparingDouble(x -> x.timestamp));
        if (idx < 0) idx = -idx - 1;
        buffer.add(idx, p);
    }

    public synchronized boolean hasBefore(double t) {
        return !buffer.isEmpty() && buffer.get(0).timestamp < t;
    }

    public synchronized Twist2d getTwistAt(double t) {
        for (int i = buffer.size() - 1; i >= 0; i--) {
            if (buffer.get(i).timestamp <= t) {
                return buffer.get(i).twist;
            }
        }
        return new Twist2d();
    }

    public synchronized double getFomAt(double t) {
        for (int i = buffer.size() - 1; i >= 0; i--) {
            if (buffer.get(i).timestamp <= t) {
                return buffer.get(i).fom;
            }
        }
        return 0.0;
    }
}
