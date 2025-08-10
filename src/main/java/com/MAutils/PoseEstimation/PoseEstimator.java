package com.MAutils.PoseEstimation;

import java.util.*;
import java.util.stream.Collectors;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.wpilibj.Timer;

public class PoseEstimator {
    private static final double HISTORY_WINDOW_SEC = 0.3; 

    private static final List<PoseEstimatorSource> sources = new ArrayList<>();
    private static Pose2d poseBeforeHistory, currentPose, tempPose;
    private static final Deque<HistoryEntry> history = new ArrayDeque<>();
    private static Deque<HistoryEntry> tempHist;
    private static List<Double> tempTimes;
    private static double sumXY, sumTh, dxSum, dySum, dthetaSum,  fusedDx, fusedDy, fusedDtheta, cutoff, wXY, wTh, now, lastUpdateTime, historyStartTime; 
    private static HistoryEntry tempEntry; 
    private static Twist2d fused;


    public static void resetPose(Pose2d newPose) {
        now = Timer.getFPGATimestamp();
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
        now = Timer.getFPGATimestamp();
        boolean late = sources.stream()
                .anyMatch(s -> s.hasBefore(lastUpdateTime));
        if (late) {
            replayHistory();
        }

        fused = computeFusedTwist(now);
        currentPose = currentPose.exp(fused);
        history.addLast(new HistoryEntry(now, fused));
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

     public static Pose2d getPoseAt(double queryTime) {
        tempPose = poseBeforeHistory;
        for (HistoryEntry e : history) {
          if (e.time > queryTime) {
            break;
          }
          tempPose = tempPose.exp(e.twist);
        }
    
        return tempPose;
      }

    private static void replayHistory() {
        tempTimes = history.stream()
                .map(e -> e.time)
                .collect(Collectors.toList());

        tempPose = poseBeforeHistory;
        tempHist = new ArrayDeque<>();

        for (double t : tempTimes) {
            fused = computeFusedTwist(t);
            tempPose = tempPose.exp(fused);
            tempHist.addLast(new HistoryEntry(t, fused));
        }

        history.clear();
        history.addAll(tempHist);
        currentPose = tempPose;
        lastUpdateTime = tempTimes.isEmpty() ? historyStartTime : tempTimes.get(tempTimes.size() - 1);
        trimHistory();
    }

    private static Twist2d computeFusedTwist(double timestamp) {
        sumXY   = 0; sumTh = 0;
        dxSum   = 0; dySum = 0; dthetaSum = 0;
    
        for (PoseEstimatorSource src : sources) {
            Twist2d tt    = src.getTwistAt(timestamp);
    
            wXY    = src.getFomXYAt(timestamp);
            dxSum       += tt.dx     * wXY;
            dySum       += tt.dy     * wXY;
            sumXY       += wXY;
    
            wTh    = src.getFomThetaAt(timestamp);
            dthetaSum   += tt.dtheta * wTh;
            sumTh       += wTh;
        }
    
        fusedDx     = (sumXY  > 0) ? dxSum     / sumXY  : 0;
        fusedDy     = (sumXY  > 0) ? dySum     / sumXY  : 0;
        fusedDtheta = (sumTh  > 0) ? dthetaSum / sumTh  : 0;
    
        return new Twist2d(fusedDx, fusedDy, fusedDtheta);
    }

    private static void trimHistory() {
        cutoff = lastUpdateTime - HISTORY_WINDOW_SEC;
        while (!history.isEmpty() && history.peekFirst().time < cutoff) {
            tempEntry = history.removeFirst();
            poseBeforeHistory = poseBeforeHistory.exp(tempEntry.twist);
            historyStartTime = tempEntry.time;
        }
    }

    private static class HistoryEntry {
        final double time;
        final Twist2d twist;

        HistoryEntry(double t, Twist2d tw) {
            time = t;
            twist = tw;
        }
    }
}
