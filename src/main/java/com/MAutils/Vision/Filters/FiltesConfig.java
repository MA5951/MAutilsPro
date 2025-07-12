package com.MAutils.Vision.Filters;

import com.MAutils.Vision.IOs.VisionCameraIO.PoseEstimateType;

public class FiltesConfig {

    public double maxAmbiguity = 0.25;                // Reject if ambiguity > this
    public double maxDistanceMeters = 6.0;            // Reject if pose is too far
    public double maxPoseJumpMeters = 1.5;            // Reject if new pose is too far from current
    public double maxDeltaAngleDegrees = 25.0;        // Reject if rotation jump is too big
    public double maxLatencyMillis = 100;             // Reject if data is too old
    public double minTagsSeen = 1;                    // Reject if fewer than this many tags seen

    public double fieldOfViewLimitXDeg = 25.0;        // Reject if tx near horizontal edge
    public double fieldOfViewLimitYDeg = 20.0;        // Reject if ty near vertical edge

    public boolean requireTagWhitelist = false;       // Enable tag whitelist
    public int[] tagWhitelist = new int[0];           // Whitelisted tag IDs (if enabled)

    public PoseEstimateType poseEstimateType = PoseEstimateType.MT2; // Use MegaTag2 for pose estimates
}
