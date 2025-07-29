package com.MAutils.PoseEstimation;

import java.util.*;
import java.util.stream.Collectors;

import com.MAutils.Logger.MALog;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.wpilibj.Timer;

/**
 * PoseEstimator holds any number of PoseEstimatorSource,
 * fuses them each loop by FOM‐weighted average, and corrects for latency
 * by replaying a short history when late measurements arrive.
 */
public class PoseEstimator {
    private static final double HISTORY_WINDOW_SEC = 0.3; // must exceed max latency

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
        // 1) Clear any pending applied‐twists
        history.clear();
        // 2) Start history window here
        poseBeforeHistory = newPose;
        historyStartTime = now;
        // 3) Reset “current” pose and last‐update time
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
        boolean late = sources.stream()
                .anyMatch(s -> s.hasBefore(lastUpdateTime));
        if (late) {
            replayHistory();
        }

        // Fuse at 'now', integrate once, push into history, trim old
        Twist2d fused = computeFusedTwist(now);
        MALog.log("/Pose Estimator/Swerve Drive Estimator/2DX", fused.dx);
        currentPose = currentPose.exp(fused);
        cumDx += fused.dx;
        MALog.log("/Pose Estimator/Swerve Drive Estimator/CumDX", cumDx);
        history.addLast(new HistoryEntry(now, fused));
        lastUpdateTime = now;
        trimHistory();

        return currentPose;
    }

    /** Your “team confidence” = average of all sources’ FOM at last update. */
    public static double getRobotFOM() {
        return sources.stream()
                .mapToDouble(s -> s.getFomXYAt(lastUpdateTime))
                .average()
                .orElse(0.0);
    }

    // —— internal —— //

    /**
     * Replay from poseBeforeHistory through all history entries, slotting in any
     * late packets.
     */

     public static synchronized Pose2d getPoseAt(double queryTime) {
        // 1) start from the pose just before our replay window
        Pose2d pose = poseBeforeHistory;
    
        // 2) step through each history entry in time order
        for (HistoryEntry e : history) {
          if (e.time > queryTime) {
            break;
          }
          pose = pose.exp(e.twist);
        }
    
        return pose;
      }


    private static void replayHistory() {
        List<Double> times = history.stream()
                .map(e -> e.time)
                .collect(Collectors.toList());

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

    /** Weighted‐average of each source’s twist at exactly timestamp T. */
    private static Twist2d computeFusedTwist(double timestamp) {
        double sumXY   = 0, sumTh = 0;
        double dxSum   = 0, dySum = 0, dthetaSum = 0;
    
        for (PoseEstimatorSource src : sources) {
            Twist2d tt    = src.getTwistAt(timestamp);
    
            // translation fusion
            double wXY    = src.getFomXYAt(timestamp);
            dxSum       += tt.dx     * wXY;
            dySum       += tt.dy     * wXY;
            sumXY       += wXY;
    
            // rotation fusion
            double wTh    = src.getFomThetaAt(timestamp);
            dthetaSum   += tt.dtheta * wTh;
            sumTh       += wTh;
        }
    
        double fusedDx     = (sumXY  > 0) ? dxSum     / sumXY  : 0;
        double fusedDy     = (sumXY  > 0) ? dySum     / sumXY  : 0;
        double fusedDtheta = (sumTh  > 0) ? dthetaSum / sumTh  : 0;
    
        return new Twist2d(fusedDx, fusedDy, fusedDtheta);
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
