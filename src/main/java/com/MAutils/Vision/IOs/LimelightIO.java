
package com.MAutils.Vision.IOs;

import com.MAutils.Vision.Util.LimelightHelpers;
import com.MAutils.Vision.Util.LimelightHelpers.PoseEstimate;
import com.MAutils.Vision.Util.LimelightHelpers.RawFiducial;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;

public class LimelightIO implements VisionCameraIO {

    private final String name;
    private RawFiducial[] blankFiducial = new RawFiducial[] { new RawFiducial(0, 0, 0, 0, 0, 0, 0) };
    private PoseEstimate blankPoseEstimate = new PoseEstimate(new Pose2d(-1 , -1, new Rotation2d()), 0, 0, 0, 0, 0, 0, blankFiducial, false);
    private RawFiducial[] fiducials;
    private PoseEstimate poseEstimate;

    public LimelightIO(String name) {
        this.name = name;
    }

    public void setCameraPosition(Transform3d positionRelaticToRobot) {
        LimelightHelpers.setCameraPose_RobotSpace(name, positionRelaticToRobot.getY(), positionRelaticToRobot.getX(),
                positionRelaticToRobot.getZ(),
                positionRelaticToRobot.getRotation().getX(), positionRelaticToRobot.getRotation().getY(),
                positionRelaticToRobot.getRotation().getZ());
    }

    public void setPipline(int pipeline) {
        LimelightHelpers.setPipelineIndex(name, pipeline);
    }

    public void setCrop(double cropXMin, double cropXMax, double cropYMin, double cropYMax) {
    }

    public void filterTags(int[] tags) {
    }

    public void takeSnapshot() {
    }

    public int getPipline() {
        return (int) LimelightHelpers.getCurrentPipelineIndex(name);
    }

    public RawFiducial getTag() {
        return getFiducials()[0];
    }

    public RawFiducial[] getFiducials() {
        fiducials = LimelightHelpers.getRawFiducials(name);
        return fiducials.length > 0 ? fiducials : blankFiducial;
    }

    public PoseEstimate getPoseEstimate(PoseEstimateType type) {
        poseEstimate = type == PoseEstimateType.MT2
                ? LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(name)
                : LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(name);

        blankPoseEstimate.isMegaTag2 = type == PoseEstimateType.MT2;

        return poseEstimate != null ? poseEstimate : blankPoseEstimate;

    }

    @Override
    public String getName() {
        return name;
    }

}
