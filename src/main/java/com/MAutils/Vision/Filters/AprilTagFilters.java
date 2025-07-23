package com.MAutils.Vision.Filters;

import java.util.function.Supplier;

import com.MAutils.Vision.IOs.VisionCameraIO;
import com.MAutils.Vision.Util.LimelightHelpers.PoseEstimate;
import com.MAutils.Vision.Util.LimelightHelpers.RawFiducial;

import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class AprilTagFilters {
    public static final double fomCoefficient = 0.1;
    public static final double ambiguityCoefficient = 0.2;

    private FiltersConfig filtersConfig;
    private VisionCameraIO visionCameraIO;
    private RawFiducial tag;
    private PoseEstimate visionPose;
    private Supplier<ChassisSpeeds> chassiSpeedsSupplier;
    @SuppressWarnings("unused")
    private ChassisSpeeds chassisSpeeds;


    private double currentFOM;

    public AprilTagFilters(FiltersConfig filtersConfig, VisionCameraIO visionCameraIO, Supplier<ChassisSpeeds> chassisSpeedsSupplier) {
        this.filtersConfig = filtersConfig;
        this.visionCameraIO = visionCameraIO;
        this.chassiSpeedsSupplier = chassisSpeedsSupplier;
    }

    public void updateFiltersConfig(FiltersConfig newConfig) {
        this.filtersConfig = newConfig;
    }

    public double getFOM() {
        tag = visionCameraIO.getTag();
        visionPose = visionCameraIO.getPoseEstimate(filtersConfig.poseEstimateType);
        chassisSpeeds = chassiSpeedsSupplier.get();
        
        if (!visionCameraIO.isTag() || !filtersConfig.fieldRactangle.contains(visionPose.pose.getTranslation()) || tag.ambiguity > 0.7
        ) return Double.MAX_VALUE;


        currentFOM = (fomCoefficient * Math.pow(visionPose.avgTagDist, 1.2) / Math.pow(visionPose.tagCount, 2)) * (1 + ambiguityCoefficient * tag.ambiguity);

        return currentFOM;

    }

}
