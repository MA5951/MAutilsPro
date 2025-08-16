package com.MAutils.Vision.Filters;

import java.util.function.Supplier;

import com.MAutils.Vision.IOs.VisionCameraIO;
import com.MAutils.Vision.Util.LimelightHelpers.PoseEstimate;
import com.MAutils.Vision.Util.LimelightHelpers.RawFiducial;

import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class AprilTagFilters {
    public static final double fomCoefficient = 0.1;

    private FiltersConfig filtersConfig;
    private VisionCameraIO visionCameraIO;
    private RawFiducial tag;
    private PoseEstimate visionPose;
    @SuppressWarnings("unused")
    private ChassisSpeeds chassisSpeeds;

    public AprilTagFilters(FiltersConfig filtersConfig, VisionCameraIO visionCameraIO,
            Supplier<ChassisSpeeds> chassisSpeedsSupplier) {
        this.filtersConfig = filtersConfig;
        this.visionCameraIO = visionCameraIO;
    }

    public void updateFiltersConfig(FiltersConfig newConfig) {
        this.filtersConfig = newConfig;
    }

    public double getXyFOM() {//Talk with rader
        tag         = visionCameraIO.getTag();
        visionPose  = visionCameraIO.getPoseEstimate(filtersConfig.poseEstimateType);
    
        // if no valid tag, out‐of‐bounds, or too ambiguous, zero confidence
        if (!visionCameraIO.isTag() || visionPose.pose.getX() == 0
            || !filtersConfig.fieldRactangle.contains(visionPose.pose.getTranslation())
            || tag.ambiguity > 0.7) {
          return 0.0;
        }
    
        
        return 0.5;
    }

    public double getOFOM() {//Talk with rader 
        if (!visionCameraIO.isTag() || visionPose.pose.getX() == 0
        || !filtersConfig.fieldRactangle.contains(visionPose.pose.getTranslation())) {
            return 0;
        }
        return 0.5;
    }
    

}
