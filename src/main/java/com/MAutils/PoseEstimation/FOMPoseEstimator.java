package com.MAutils.PoseEstimation;

import com.MAutils.Logger.MALog;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

//TODO give this to ari to final check

public class FOMPoseEstimator extends SubsystemBase { //TODO why its extends SubsystemBase
    //TODO need to rewrite the calculations to use what orbit do
    private static FOMPoseEstimator instance;

    private Pose2d currentPose;
    //TODO add lastPose
    //TODO add lastVisionPose

    //TODO think we need to add her filters for make change 
    //TODO add the filter off outfrom fild
    //TODO add the filter off in obstacle
    //TODO add more then one camera filter
    //TODO add lookahead fucn
    

    private double maxOdometryWeight = 1.0;  //TODO what the point of this?
    private double maxGyroWeight = 1.0; //TODO what the point of this?
    private double maxVisionWeight = 1.0; //TODO what the point of this?


    public FOMPoseEstimator(Pose2d initialPose) { //TODO if use singalton this need to be private. why its not a static class?
        this.currentPose = initialPose;
    }


    //TODO all the add odometry fucn, why not use the odometry func of wpi its not the same as yours
    public void addTranslationDelta(Twist2d delta, double translationFOM) { //TODO change to use only with the swerve subsystem
         //TODO chage the translationFOM to suplier that we pass once
        double weight = MathUtil.clamp(translationFOM, 0.0, 1.0) * maxOdometryWeight;
        currentPose = currentPose.exp(new Twist2d(delta.dx * weight, delta.dy * weight, 0.0));
    }

    public void addRotationDelta(double dtheta, double rotationFOM) { //TODO change to use only with the vision class
        //TODO chage the rotationFOM to suplier that we pass once
        if (rotationFOM <= 0.0)
            return;
        double weight = MathUtil.clamp(rotationFOM, 0.0, 1.0) * maxGyroWeight;
        currentPose = currentPose.exp(new Twist2d(0.0, 0.0, dtheta * weight));
    }

    public void addVisionMeasurement(Pose2d visionPose, double visionFOM) { //TODO need to see how wpi do this, they also give weight to Latency
        //TODO chage the visionFOM to suplier that we pass once
        if (visionFOM <= 0.0)
            return;
        double weight = MathUtil.clamp(visionFOM, 0.0, 1.0) * maxVisionWeight;
        currentPose = weightedAverage(currentPose, visionPose, weight);
    }

    //TODO add func of addMeasutement

    public Pose2d getEstimatedPose() {
        return currentPose;
    }

    public void resetPose(Pose2d newPose) {
        this.currentPose = newPose;
    }

    public void setMaxWeights(double odometryWeight, double gyroWeight, double visionWeight) {
        //TODO need to clamp this too
        //TODO split to 3 fucn 
        this.maxOdometryWeight = odometryWeight;
        this.maxVisionWeight = visionWeight;
        this.maxGyroWeight = gyroWeight;
    }

    private Pose2d weightedAverage(Pose2d a, Pose2d b, double weight) {
            //TODO need to rewrite the calculations to use what orbit do
        return new Pose2d(MathUtil.interpolate(a.getX(), b.getX(), weight),
                MathUtil.interpolate(a.getY(), b.getY(), weight), a.getRotation().interpolate(b.getRotation(), weight));
    }

    public static FOMPoseEstimator getInstance() {
        if (instance == null) {
            instance = new FOMPoseEstimator(new Pose2d(10.43, 5.66, new Rotation2d()));
        }
        return instance;
    }

    @Override
    public void periodic() {
        MALog.log("/Pose Estimator/Pose", currentPose);
    }
}
