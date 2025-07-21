package com.MAutils.PoseEstimation;

import com.MAutils.Logger.MALog;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;

public class FOMPoseEstimator extends SubsystemBase {
    private static Pose2d currentPose = new Pose2d();
    private static double currentFOM = 1;
    private static final TimeInterpolatableBuffer<Pose2d> poseHistory =
        TimeInterpolatableBuffer.createBuffer(2.0);

        public void addTwistMeasurement(Twist2d delta, double odomFOM, double timestamp) {
            Optional<Pose2d> optPast = poseHistory.getSample(timestamp);
            if (optPast.isEmpty()) {
                return;
            }
            Pose2d pastPose = optPast.get();
        
            Pose2d measuredPose = pastPose.exp(delta);
        
            double varP = currentFOM * currentFOM;
            double varM = odomFOM    * odomFOM;
            double wP   = 1.0 / varP;
            double wM   = 1.0 / varM;
            double alpha = wM / (wP + wM);
        
            Pose2d fusedPast = new Pose2d(
                MathUtil.interpolate(pastPose.getX(), measuredPose.getX(), alpha),
                MathUtil.interpolate(pastPose.getY(), measuredPose.getY(), alpha),
                pastPose.getRotation().interpolate(measuredPose.getRotation(), alpha)
            );
            double fusedFOM = Math.sqrt(1.0 / (wP + wM));
        
            Pose2d newPose = fusedPast;
            double lastT = timestamp;
            NavigableMap<Double,Pose2d> buffer = poseHistory.getInternalBuffer();
            for (Map.Entry<Double,Pose2d> e : buffer.tailMap(timestamp, false).entrySet()) {
                double t = e.getKey();
                Pose2d rec = e.getValue();
                Pose2d prev = poseHistory.getSample(lastT).orElse(rec);
                Twist2d d = new Twist2d(
                    rec.getX() - prev.getX(),
                    rec.getY() - prev.getY(),
                    rec.getRotation().getRadians() - prev.getRotation().getRadians()
                );
                newPose = newPose.exp(d);
                lastT = t;
            }
        
            currentPose = newPose;
            currentFOM  = fusedFOM;
            poseHistory.clear();
            poseHistory.addSample(Timer.getFPGATimestamp(), currentPose);
        }

    @Override
    public void periodic() {
        poseHistory.addSample(Timer.getFPGATimestamp(), currentPose);
        MALog.log("/Pose Estimator/Pose", currentPose);
    }

    public Pose2d getEstimatedPose() {
        return currentPose;
    }

    public double getCurrentFOM() {
        return currentFOM;
    }

    public void resetPose(Pose2d newPose, double newFOM) {
        currentPose = newPose;
        currentFOM  = newFOM;
        poseHistory.clear();
        poseHistory.addSample(Timer.getFPGATimestamp(), currentPose);
    }
}
