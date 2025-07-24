package com.MAutils.Vision.Filters;

import java.util.function.Supplier;

import com.MAutils.Vision.IOs.VisionCameraIO;
import com.MAutils.Vision.Util.LimelightHelpers.PoseEstimate;
import com.MAutils.Vision.Util.LimelightHelpers.RawFiducial;

import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class AprilTagFilters {
    public static final double fomCoefficient = 0.1;
    public static final double ambiguityCoefficient = 2;

    private FiltersConfig filtersConfig;
    private VisionCameraIO visionCameraIO;
    private RawFiducial tag;
    private PoseEstimate visionPose;
    private Supplier<ChassisSpeeds> chassiSpeedsSupplier;
    @SuppressWarnings("unused")
    private ChassisSpeeds chassisSpeeds;

    private double currentFOM;

    public AprilTagFilters(FiltersConfig filtersConfig, VisionCameraIO visionCameraIO,
            Supplier<ChassisSpeeds> chassisSpeedsSupplier) {
        this.filtersConfig = filtersConfig;
        this.visionCameraIO = visionCameraIO;
        this.chassiSpeedsSupplier = chassisSpeedsSupplier;
    }

    public void updateFiltersConfig(FiltersConfig newConfig) {
        this.filtersConfig = newConfig;
    }

    public double getFOM() {
        tag         = visionCameraIO.getTag();
        visionPose  = visionCameraIO.getPoseEstimate(filtersConfig.poseEstimateType);
    
        // if no valid tag, out‐of‐bounds, or too ambiguous, zero confidence
        if (!visionCameraIO.isTag() || visionPose.pose.getX() == 0
            || !filtersConfig.fieldRactangle.contains(visionPose.pose.getTranslation())
            || tag.ambiguity > 0.7) {
          return 0.0;
        }
    
        // avoid divide‐by‐zero:
        int count = Math.max(1, visionPose.tagCount);
    
        // compute your “sigma” in a local variable
        double sigma = fomCoefficient
                     * Math.pow(visionPose.avgTagDist, 1.2)
                     / (count * count)
                     * (1 + ambiguityCoefficient * (tag.ambiguity - 0.3));
    
        // if sigma is NaN or negative, treat as “no confidence”
        if (Double.isNaN(sigma) || sigma < 0) {
          return 0.0;
        }
    
        // map σ → [0…1] via 1/(1+σ)
        double raw = 1.0 / (1.0 + sigma);
    
        // clamp and handle infinities
        if (!Double.isFinite(raw)) {
          return 0.0;
        }
        return Math.max(0.0, Math.min(1.0, raw));
    }
    

}
