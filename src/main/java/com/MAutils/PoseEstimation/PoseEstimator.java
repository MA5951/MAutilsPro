package com.MAutils.PoseEstimation;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;

/**
 * PoseEstimator holds any number of PoseEstimatorSource,
 * fuses them each loop by XY/θ FOM-weighted averages, and corrects for latency
 * by replaying a short history when late measurements arrive.
 */
public class PoseEstimator {
    private static final double HISTORY_WINDOW_SEC = 0.5; // must exceed max latency

    private static final List<PoseEstimatorSource> sources = new ArrayList<>();

    // Pose and time just before our replay buffer begins
    private static Pose2d poseBeforeHistory;
    private static double historyStartTime;

    // Recent applied twists, for replay
    private static final Deque<HistoryEntry> history = new ArrayDeque<>();

    private static Pose2d currentPose;
    private static double lastUpdateTime;

    private static double cumDx = 0;

    public static void resetPose(Pose2d newPose) {
        double now = Timer.getFPGATimestamp();
        history.clear();
        poseBeforeHistory = newPose;
        historyStartTime = now;
        currentPose = newPose;
        lastUpdateTime = now;
    }

    /** Add odometry / limelight / any other source */
    public static void addSource(PoseEstimatorSource src) {
        sources.add(src);
    }

    /** Call once per robot loop. Returns the updated pose. */
    public static Pose2d update() {
        double now = Timer.getFPGATimestamp();

        // If any source has a measurement stamped before our last update, we need to
        // replay
        boolean late = sources.stream().anyMatch(s -> s.hasBefore(lastUpdateTime));
        if (late) {
            replayHistory();
        }

        // Fuse at 'now', integrate once, push into history, trim old
        Twist2d fused = computeFusedTwist(now);
        currentPose = currentPose.exp(fused);
        history.addLast(new HistoryEntry(now, fused));
        lastUpdateTime = now;
        trimHistory();

        return currentPose;
    }

    /** Backward-compatible overall FOM (mean of XY and θ across sources). */
    public static double getRobotFOM() {
        return sources.stream()
                .mapToDouble(s -> s.getFomAt(lastUpdateTime))
                .average()
                .orElse(0.0);
    }

    /** New: average XY FOM across sources at the last update. */
    public static double getRobotFOMXY() {
        return sources.stream()
                .mapToDouble(s -> s.getFomXYAt(lastUpdateTime))
                .average()
                .orElse(0.0);
    }

    public static Pose2d getPoseIn(double time, ChassisSpeeds speedsRobotRelativ) {
        return currentPose.exp(
                new Twist2d(speedsRobotRelativ.vxMetersPerSecond * time, speedsRobotRelativ.vyMetersPerSecond * time,
                        speedsRobotRelativ.omegaRadiansPerSecond * time));
    }

    // —— internal —— //

    /** Pose reconstructed by applying history up to a query time. */
    public static Pose2d getPoseAt(double queryTime) {
        Pose2d pose = poseBeforeHistory;
        for (HistoryEntry e : history) {
            if (e.time > queryTime)
                break;
            pose = pose.exp(e.twist);
        }
        return pose;
    }

    /**
     * Replay from poseBeforeHistory through all history entries, slotting in any
     * late packets.
     */
    private static void replayHistory() {
        List<Double> times = history.stream().map(e -> e.time).collect(Collectors.toList());

        Pose2d pose = poseBeforeHistory;
        Deque<HistoryEntry> newHist = new ArrayDeque<>();

        for (double t : times) {
            Twist2d f = computeFusedTwist(t);
            pose = pose.exp(f);
            newHist.addLast(new HistoryEntry(t, f));
        }

        history.clear();
        history.addAll(newHist);
        currentPose = pose;
        lastUpdateTime = times.isEmpty() ? historyStartTime : times.get(times.size() - 1);
        trimHistory();
    }

    /**
     * Weighted average of each source’s twist at exactly timestamp T (XY vs θ
     * separated).
     */
    private static Twist2d computeFusedTwist(double timestamp) {
        double sumFomXY = 0.0, sumFomTheta = 0.0;
        double dx = 0.0, dy = 0.0, dTheta = 0.0;

        for (var src : sources) {
            Twist2d tt = src.getTwistAt(timestamp);
            double fXY = src.getFomXYAt(timestamp);
            double fTh = src.getFomThetaAt(timestamp);

            dx += tt.dx * fXY;
            dy += tt.dy * fXY;
            dTheta += tt.dtheta * fTh;

            sumFomXY += fXY;
            sumFomTheta += fTh;
        }

        double outDx = dx / sumFomXY;
        double outDy = dy / sumFomXY;
        double outDTh = dTheta / sumFomTheta;

        if (sumFomXY <= 0.0 && sumFomTheta <= 0.0)
            return new Twist2d();
        return new Twist2d(outDx, outDy, outDTh);
    }

    /** Drop anything older than our window and roll poseBeforeHistory forward. */
    private static void trimHistory() {
        double cutoff = lastUpdateTime - HISTORY_WINDOW_SEC;
        while (!history.isEmpty() && history.peekFirst().time < cutoff) {
            var e = history.removeFirst();
            poseBeforeHistory = poseBeforeHistory.exp(e.twist);
            historyStartTime = e.time;
        }
    }

    /** One applied‐twist entry. */
    private static class HistoryEntry {
        final double time;
        final Twist2d twist;

        HistoryEntry(double t, Twist2d tw) {
            time = t;
            twist = tw;
        }
    }
}
