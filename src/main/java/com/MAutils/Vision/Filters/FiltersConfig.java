package com.MAutils.Vision.Filters;

import com.MAutils.Vision.IOs.VisionCameraIO.PoseEstimateType;

import edu.wpi.first.math.geometry.Rectangle2d;
import edu.wpi.first.math.geometry.Translation2d;

public class FiltersConfig {

    public double maxAmbiguity = 0.25; // Reject if ambiguity > this
    public double maxDistanceMeters = 6.0; // Reject if pose is too far
    public double maxPoseJumpMeters = 1.5; // Reject if new pose is too far from current
    public double maxDeltaAngleDegrees = 25.0; // Reject if rotation jump is too big
    public double maxLatencyMillis = 100; // Reject if data is too old
    public double minTagsSeen = 1; // Reject if fewer than this many tags seen

    public double fieldOfViewLimitXDeg = 25.0; // Reject if tx near horizontal edge
    public double fieldOfViewLimitYDeg = 20.0; // Reject if ty near vertical edge

    public boolean requireTagWhitelist = false; // Enable tag whitelist

    public static final Translation2d FieldZeroCorner = new Translation2d(0, 0);
    public static final Translation2d FieldFarCorner = new Translation2d(17.548, 8.052);
    public static final Translation2d FieldMiddlePoint = new Translation2d(17.548 / 2, 8.052 / 2);
    public final Rectangle2d fieldRactangle = new Rectangle2d(FieldZeroCorner, FieldFarCorner);

    public PoseEstimateType poseEstimateType = PoseEstimateType.MT2; // Use MegaTag2 for pose estimates

    public FiltersConfig() {
    }
}
