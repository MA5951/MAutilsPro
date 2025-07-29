package com.MAutils.PoseEstimation;

import java.util.*;

import com.MAutils.Utils.Constants;

import edu.wpi.first.math.geometry.Twist2d;

public class PoseEstimatorSource {
    private static class Measurment {
        @SuppressWarnings("unused")
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

    public PoseEstimatorSource() {
        PoseEstimator.addSource(this);
    }

    public synchronized void addMeasurement(
            Twist2d delta, double fomXY, double fomTheta, double timestamp) {
        // avoid zero‐FOMs
        fomXY = fomXY <= 0 ? fomXY : Constants.EPSILON;
        fomTheta = fomTheta <= 0 ? fomTheta : Constants.EPSILON;
        Measurment m = new Measurment(delta, fomXY, fomTheta, timestamp);
        int idx = Collections.binarySearch(buffer, m,
                Comparator.comparingDouble(xm -> xm.timestamp));
        if (idx < 0)
            idx = -idx - 1;
        buffer.add(idx, m);
    }

    public synchronized boolean hasBefore(double t) {
        return !buffer.isEmpty() && buffer.get(0).timestamp < t;
    }

    public synchronized Twist2d getTwistAt(double t) {
        for (int i = buffer.size() - 1; i >= 0; --i) {
            if (buffer.get(i).timestamp <= t) {
                return buffer.get(i).twist;
            }
        }
        return blankTwist;
    }

    public synchronized double getFomXYAt(double t) {
        for (int i = buffer.size() - 1; i >= 0; --i) {
            if (buffer.get(i).timestamp <= t) {
                return buffer.get(i).fomXY;
            }
        }
        return Constants.EPSILON;
    }

    public synchronized double getFomThetaAt(double t) {
        for (int i = buffer.size() - 1; i >= 0; --i) {
            if (buffer.get(i).timestamp <= t) {
                return buffer.get(i).fomTheta;
            }
        }
        return Constants.EPSILON;
    }
}
