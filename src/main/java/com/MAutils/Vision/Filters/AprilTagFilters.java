// package com.MAutils.Vision.Filters;

// import com.MAutils.Vision.IOs.VisionCameraIO;
// import com.MAutils.Vision.Util.LimelightHelpers.PoseEstimate;
// import com.MAutils.Vision.Util.LimelightHelpers.RawFiducial;

// import edu.wpi.first.math.geometry.Pose2d;
// import edu.wpi.first.math.geometry.Rotation2d;

// public class AprilTagFilters {

//     private final FiltesConfig filtersConfig;
//     private VisionCameraIO visionCameraIO;
//     private RawFiducial tag;
//     private PoseEstimate visionPose;

//     public AprilTagFilters(FiltesConfig filtersConfig, VisionCameraIO visionCameraIO) {
//         this.filtersConfig = filtersConfig;
//         this.visionCameraIO = visionCameraIO;
//     }

//     public double getFOM() {
//         tag = visionCameraIO.getTag();
//         visionPose = visionCameraIO.getPoseEstimate(filtersConfig.poseEstimateType);
//         if ((tag.ambiguity > filtersConfig.maxAmbiguity) || )

//         double distance = currentPose.getTranslation().getDistance(visionPose.getTranslation());
//         if (distance > filtersConfig.maxPoseJumpMeters)
//             return 0.0;

//         double deltaAngle = Math.abs(currentPose.getRotation().minus(visionPose.getRotation()).getDegrees());
//         if (deltaAngle > filtersConfig.maxDeltaAngleDegrees)
//             return 0.0;

//         double visionZ = visionPose.getTranslation().getNorm();
//         if (visionZ > filtersConfig.maxDistanceMeters)
//             return 0.0;

//         if (Math.abs(tx) > filtersConfig.fieldOfViewLimitXDeg)
//             return 0.0;
//         if (Math.abs(ty) > filtersConfig.fieldOfViewLimitYDeg)
//             return 0.0;

//         // ✅ Passed filters — calculate FOM

//         double ambiguityScore = 1.0 - (ambiguity / filtersConfig.maxAmbiguity);
//         double distanceScore = 1.0 - Math.min(visionZ / filtersConfig.maxDistanceMeters, 1.0);
//         double tagCountScore = Math.min(tagsSeen / 2, 1.0); // 2 tags = perfect
//         double fovScore = 1.0 - (Math.max(Math.abs(tx) / filtersConfig.fieldOfViewLimitXDeg,
//                 Math.abs(ty) / filtersConfig.fieldOfViewLimitYDeg));

//         double fom = (ambiguityScore * 0.4) +
//                 (distanceScore * 0.2) +
//                 (tagCountScore * 0.2) +
//                 (fovScore * 0.2);

//         return clamp(fom, 0.0, 1.0);
//     }

//     private boolean isTagWhitelisted(int tagID) {
//         for (int id : filtersConfig.tagWhitelist) {
//             if (id == tagID)
//                 return true;
//         }
//         return false;
//     }

//     private double clamp(double value, double min, double max) {
//         return Math.max(min, Math.min(value, max));
//     }
// }
