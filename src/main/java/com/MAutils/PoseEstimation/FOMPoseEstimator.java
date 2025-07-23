package com.MAutils.PoseEstimation;

import com.MAutils.Logger.MALog;
import com.MAutils.Utils.Constants;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;

public class FOMPoseEstimator extends SubsystemBase {// FOM

    private static Pose2d currentPose = new Pose2d();
    private static double currentFOM = 1;
    private static final TimeInterpolatableBuffer<Pose2d> poseHistory = TimeInterpolatableBuffer.createBuffer(2.0);

    private static PoseEstimatorSource[] sources;


    public FOMPoseEstimator() {
        super();
        resetPose(new Pose2d(2,2,new Rotation2d()), Constants.EPSILON);
    }

    public static void setSources(PoseEstimatorSource... poseEstimatorSources) {
        sources = poseEstimatorSources;
    }

    public static void addTwistMeasurement(Twist2d delta, double odomFOM, double timestamp) {
        Optional<Pose2d> optPast = poseHistory.getSample(timestamp);
        if (optPast.isEmpty()) {
            return;
        }
        Pose2d pastPose = optPast.get();

        Pose2d measuredPose = pastPose.exp(delta);

        double varP = currentFOM * currentFOM;
        double varM = odomFOM * odomFOM;
        double wP = 1.0 / varP;
        double wM = 1.0 / varM;
        double alpha = wM / (wP + wM);
        System.out.printf("odomFOM=%.4f  curFOM=%.4f  α=%.4f%n", odomFOM, currentFOM, alpha);

        Pose2d fusedPast = new Pose2d(
                MathUtil.interpolate(pastPose.getX(), measuredPose.getX(), alpha),
                MathUtil.interpolate(pastPose.getY(), measuredPose.getY(), alpha),
                pastPose.getRotation().interpolate(measuredPose.getRotation(), alpha));
        double fusedFOM = Math.sqrt(1.0 / (wP + wM));

        Pose2d newPose = fusedPast;
        double lastT = timestamp;
        NavigableMap<Double, Pose2d> buffer = poseHistory.getInternalBuffer();
        for (Map.Entry<Double, Pose2d> e : buffer.tailMap(timestamp, false).entrySet()) {
            double t = e.getKey();
            Pose2d rec = e.getValue();
            Pose2d prev = poseHistory.getSample(lastT).orElse(rec);
            Twist2d d = new Twist2d(
                    rec.getX() - prev.getX(),
                    rec.getY() - prev.getY(),
                    rec.getRotation().getRadians() - prev.getRotation().getRadians());
            newPose = newPose.exp(d);
            lastT = t;
        }

        currentPose = newPose;
        currentFOM = Constants.EPSILON * 2;
        poseHistory.clear();
        poseHistory.addSample(Timer.getFPGATimestamp(), currentPose);
    }

    public static Pose2d getPoseAtTime(double timestamp) {
        return poseHistory.getSample(timestamp).get();
    }

    @Override
    public void periodic() {
        poseHistory.addSample(Timer.getFPGATimestamp(), currentPose);
        MALog.log("/Pose Estimator/Pose", currentPose);
    }

    public static Pose2d getEstimatedPose() {
        return currentPose;
    }

    public static double getCurrentFOM() {
        return currentFOM;
    }

    public void resetPose(Pose2d newPose, double newFOM) {
        currentPose = newPose;
        currentFOM = newFOM;
        poseHistory.clear();
        poseHistory.addSample(Timer.getFPGATimestamp(), currentPose);
    }
}
