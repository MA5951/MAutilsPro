package com.MAutils.PoseEstimation;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.wpilibj.Timer;

public class PoseEstimator {
    /** Rolling replay window (seconds). */
    private static final double HISTORY_WINDOW_SEC = 0.5;
    private static final int MAX_HISTORY_SAMPLES = (int) (HISTORY_WINDOW_SEC / 0.02); 

    private static final List<PoseEstimatorSource> sources = new ArrayList<>();

    // Pose and anchor just before our rolling window
    private static Pose2d poseBeforeHistory;
    private static Pose2d currentPose;

    // Chronological fused twist history keyed by timestamp
    private static final NavigableMap<Double, Twist2d> history = new TreeMap<>();

    private static double lastUpdateTime;
    private static double historyStartTime;

    // scratch (avoid per-loop allocs)
    private static double sumXY, sumTh, dxSum, dySum, dthetaSum, wXY, wTh;

    public static void resetPose(Pose2d newPose) {
        double now = Timer.getFPGATimestamp();
        history.clear();
        poseBeforeHistory = newPose;
        historyStartTime = now;
        currentPose = newPose;
        lastUpdateTime = now;
    }

    public static void addSource(PoseEstimatorSource src) {
        sources.add(src);
    }

    public static Pose2d update() {
        double now = Timer.getFPGATimestamp();

        // If any source has samples earlier than our last fused update, replay
        boolean late = sources.stream().anyMatch(s -> s.hasBefore(lastUpdateTime));
        if (late) {
            replayHistory();
        }

        Twist2d fused = computeFusedTwist(now);
        currentPose = currentPose.exp(fused);
        history.put(now, fused);
        lastUpdateTime = now;

        trimHistory();
        return currentPose;
    }

    public static double getRobotFOM() {
        return sources.stream()
                .mapToDouble(s -> s.getFomXYAt(lastUpdateTime))
                .average()
                .orElse(0.0);
    }

    /** Integrate fused twists up to queryTime, starting from the anchor pose. */
    public static Pose2d getPoseAt(double queryTime) {
        Pose2d pose = poseBeforeHistory;
        for (var e : history.entrySet()) {
            if (e.getKey() > queryTime)
                break;
            pose = pose.exp(e.getValue());
        }
        return pose;
    }

    /**
     * Recompute the fused history over the current timestamps using sources’
     * time-ordered buffers.
     */
    private static void replayHistory() {
        if (history.isEmpty()) {
            currentPose = poseBeforeHistory;
            lastUpdateTime = historyStartTime;
            return;
        }

        var times = new ArrayList<>(history.keySet()); // chronological
        Pose2d pose = poseBeforeHistory;
        history.clear();

        for (double t : times) {
            Twist2d fused = computeFusedTwist(t);
            pose = pose.exp(fused);
            history.put(t, fused);
        }

        currentPose = pose;
        lastUpdateTime = times.get(times.size() - 1);
        trimHistory();
    }

    /** Weighted-average fusion of all sources at a specific timestamp. */
    private static Twist2d computeFusedTwist(double timestamp) {
        sumXY = sumTh = 0.0;
        dxSum = dySum = dthetaSum = 0.0;

        for (PoseEstimatorSource src : sources) {
            Twist2d tt = src.getTwistAt(timestamp);

            wXY = src.getFomXYAt(timestamp);
            dxSum += tt.dx * wXY;
            dySum += tt.dy * wXY;
            sumXY += wXY;

            wTh = src.getFomThetaAt(timestamp);
            dthetaSum += tt.dtheta * wTh;
            sumTh += wTh;
        }

        double fusedDx = (sumXY > 0.0) ? dxSum / sumXY : 0.0;
        double fusedDy = (sumXY > 0.0) ? dySum / sumXY : 0.0;
        double fusedDtheta = (sumTh > 0.0) ? dthetaSum / sumTh : 0.0;

        return new Twist2d(fusedDx, fusedDy, fusedDtheta);
    }

    /**
     * Keep only entries within the rolling window and a count cap; advance the
     * anchor accordingly.
     */
    private static void trimHistory() {
        // Time-based prune
        double cutoff = lastUpdateTime - HISTORY_WINDOW_SEC;
        var toDrop = history.headMap(cutoff, false); // strictly earlier than cutoff
        if (!toDrop.isEmpty()) {
            for (var e : new TreeMap<>(toDrop).entrySet()) { // copy to avoid CME
                poseBeforeHistory = poseBeforeHistory.exp(e.getValue());
                historyStartTime = e.getKey();
            }
            toDrop.clear(); // remove from original view
        }

        // Count-based prune (if window is very dense)
        while (history.size() > MAX_HISTORY_SAMPLES) {
            var oldest = history.pollFirstEntry();
            if (oldest != null) {
                poseBeforeHistory = poseBeforeHistory.exp(oldest.getValue());
                historyStartTime = oldest.getKey();
            }
        }
    }
}
