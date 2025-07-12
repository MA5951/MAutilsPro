package com.MAutils.PoseEstimation;

import com.MAutils.Logger.MALog;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class FOMPoseEstimator extends SubsystemBase {
    private static FOMPoseEstimator instance;

    private Pose2d currentPose;

    private double maxOdometryWeight = 1.0;
    private double maxGyroWeight = 1.0;
    private double maxVisionWeight = 1.0;

    public FOMPoseEstimator(Pose2d initialPose) {
        this.currentPose = initialPose;
    }

    public void addTranslationDelta(Twist2d delta, double translationFOM) {
        if (translationFOM <= 0.0) return;
        double weight = MathUtil.clamp(translationFOM, 0.0, 1.0) * maxOdometryWeight;
        currentPose = currentPose.exp(new Twist2d(delta.dx * weight, delta.dy * weight, 0.0));
    }

    public void addRotationDelta(double dtheta, double rotationFOM) {
        if (rotationFOM <= 0.0) return;
        double weight = MathUtil.clamp(rotationFOM, 0.0, 1.0) * maxGyroWeight;
        currentPose = currentPose.exp(new Twist2d(0.0, 0.0, dtheta * weight));
    }

    public void addVisionMeasurement(Pose2d visionPose, double visionFOM) {
        if (visionFOM <= 0.0) return;
        double weight = MathUtil.clamp(visionFOM, 0.0, 1.0) * maxVisionWeight;
        currentPose = weightedAverage(currentPose, visionPose, weight);
    }

    public Pose2d getEstimatedPose() {
        return currentPose;
    }

    public void resetPose(Pose2d newPose) {
        this.currentPose = newPose;
    }

    public void setMaxWeights(double odometryWeight, double gyroWeight ,double visionWeight) {
        this.maxOdometryWeight = odometryWeight;
        this.maxVisionWeight = visionWeight;
        this.maxGyroWeight = gyroWeight;
    }

    private Pose2d weightedAverage(Pose2d a, Pose2d b, double weight) {
        double x = MathUtil.interpolate(a.getX(), b.getX(), weight);
        double y = MathUtil.interpolate(a.getY(), b.getY(), weight);
        Rotation2d rot = a.getRotation().interpolate(b.getRotation(), weight);
        return new Pose2d(x, y, rot);
    }

    public static FOMPoseEstimator getInstance() {
        if (instance == null) {
            instance = new FOMPoseEstimator(new Pose2d(2, 2, new Rotation2d()));
        }
        return instance;
    }

    @Override
    public void periodic() {
        MALog.log("/Subsystems/Pose Estimation/Pose", currentPose);
    }
}
