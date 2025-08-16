
package com.MAutils.Vision.IOs;

import java.util.function.Supplier;

import com.MAutils.Utils.Constants;
import com.MAutils.Utils.Constants.SimulationType;
import com.MAutils.Vision.Util.LimelightHelpers;
import com.MAutils.Vision.Util.LimelightHelpers.PoseEstimate;
import com.MAutils.Vision.Util.LimelightHelpers.RawFiducial;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Robot;

public class LimelightIO implements VisionCameraIO {

    private final String logName, cameraName;
    private RawFiducial[] blankFiducial = new RawFiducial[] { new RawFiducial(0, 0, 0, 0, 0, 0, 0) }, fiducials;
    private PoseEstimate blankPoseEstimate = new PoseEstimate(new Pose2d(-1, -1, new Rotation2d()), 0, 0, 0, 0, 0, 0,
            blankFiducial, false), poseEstimate;
    private Supplier<Rotation3d> robotRotaionSupplier;
    private Rotation3d robotRotation;

    public LimelightIO(String name, Supplier<Rotation3d> robotRotaion) {
        robotRotaionSupplier = robotRotaion;

        if (!Robot.isReal() && Constants.SIMULATION_TYPE == SimulationType.REPLAY) {
            this.cameraName = "Replay/NT:/" + name;
        } else {
            this.cameraName = name;
        }

        logName = name;

    }

    public void setCameraPosition(Transform3d positionRelaticToRobot) {
        LimelightHelpers.setCameraPose_RobotSpace(cameraName, positionRelaticToRobot.getY(),
                positionRelaticToRobot.getX(),
                positionRelaticToRobot.getZ(),
                positionRelaticToRobot.getRotation().getX(), positionRelaticToRobot.getRotation().getY(),
                positionRelaticToRobot.getRotation().getZ());
    }

    public void setPipline(int pipeline) {
        LimelightHelpers.setPipelineIndex(cameraName, pipeline);
    }

    public void setCrop(double cropXMin, double cropXMax, double cropYMin, double cropYMax) {
        LimelightHelpers.setCropWindow(cameraName, cropXMin, cropXMax, cropYMin, cropYMax);
    }

    public void allowTags(int[] tags) {
        LimelightHelpers.SetFiducialIDFiltersOverride(cameraName, tags);
    }

    public void takeSnapshot() {
        LimelightHelpers.takeSnapshot(cameraName, "Snapshot_" + Timer.getFPGATimestamp());
    }

    public int getPipline() {
        return (int) LimelightHelpers.getCurrentPipelineIndex(cameraName);
    }

    public boolean isTag() {
        return LimelightHelpers.getTV(cameraName);
    }

    public RawFiducial getTag() {
        return getFiducials()[0];
    }

    public RawFiducial[] getFiducials() {
        fiducials = LimelightHelpers.getRawFiducials(cameraName);
        return fiducials.length > 0 ? fiducials : blankFiducial;
    }

    public PoseEstimate getPoseEstimate(PoseEstimateType type) {
        poseEstimate = type == PoseEstimateType.MT2
                ? LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(cameraName)
                : LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(cameraName);

        blankPoseEstimate.isMegaTag2 = type == PoseEstimateType.MT2;

        return poseEstimate != null ? poseEstimate : blankPoseEstimate;

    }

    @Override
    public String getName() {
        return logName;
    }

    public void update() {
        robotRotation = robotRotaionSupplier.get();

        LimelightHelpers.SetRobotOrientation(cameraName, robotRotation.getZ(), 0, robotRotation.getY(), 0,
                robotRotation.getX(), 0);
    }

}
