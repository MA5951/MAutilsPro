package com.MAutils.Vision.Filters;

import java.util.function.Supplier;

import com.MAutils.Vision.IOs.VisionCameraIO;
import com.MAutils.Vision.Util.LimelightHelpers.PoseEstimate;
import com.MAutils.Vision.Util.LimelightHelpers.RawFiducial;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class AprilTagFilters {
    public static final double fomCoefficient = 0.1;

    private FiltesConfig filtersConfig;
    private VisionCameraIO visionCameraIO;
    private RawFiducial tag;
    private PoseEstimate visionPose;
    private Supplier<ChassisSpeeds> chassiSpeedsSupplier;
    private ChassisSpeeds chassisSpeeds;

    private double velocityFOM;
    private double ambiguityFOM;
    private double distanceFOM;
    private double poseJumpFOM;
    private double tagsFOM;

    private double currentFOM;

    public AprilTagFilters(FiltesConfig filtersConfig, VisionCameraIO visionCameraIO, Supplier<ChassisSpeeds> chassisSpeedsSupplier) {
        this.filtersConfig = filtersConfig;
        this.visionCameraIO = visionCameraIO;
        this.chassiSpeedsSupplier = chassisSpeedsSupplier;
    }

    public void updateFiltersConfig(FiltesConfig newConfig) {
        this.filtersConfig = newConfig;
    }

    public double getFOM() {
        tag = visionCameraIO.getTag();
        visionPose = visionCameraIO.getPoseEstimate(filtersConfig.poseEstimateType);
        chassisSpeeds = chassiSpeedsSupplier.get();
        
        if (!visionCameraIO.isTag() || !filtersConfig.fieldRactangle.contains(visionPose.pose.getTranslation()) 
        ) return Double.MAX_VALUE;


        currentFOM = fomCoefficient * Math.pow(visionPose.avgTagDist, 1.2) / Math.pow(visionPose.tagCount, 2);



    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }
}
