
package com.MAutils.Vision.IOs;


import java.util.function.Supplier;

import com.MAutils.Logger.MALog;
import com.MAutils.PoseEstimation.PoseEstimator;
import com.MAutils.PoseEstimation.PoseEstimatorSource;
import com.MAutils.Vision.Filters.AprilTagFilters;
import com.MAutils.Vision.Filters.FiltersConfig;
import com.MAutils.Vision.Util.LimelightHelpers.PoseEstimate;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

public class AprilTagCamera extends Camera {

    private final PoseEstimatorSource poseEstimatorSource;
    public final Supplier<Double> robotAngleSupplier;

    private FiltersConfig teleopConfig, autoConfig;
    private AprilTagFilters aprilTagFilters;
    private Transform2d delta;
    private PoseEstimate poseEstimate;
    private Pose2d visionPose, prior;
    private Rotation2d heading;
    private Twist2d visionTwsit = new Twist2d();
    private boolean updatePoseEstiamte = true;
    private double xyFom, oFom, visionTs, fieldDx, fieldDy, fieldDtheta, robotDx, robotDy;

    public AprilTagCamera(VisionCameraIO cameraIO, FiltersConfig filterConfig, Supplier<Double> robotAngleSupplier) {
        super(cameraIO);

        this.teleopConfig = filterConfig;
        this.autoConfig = filterConfig;

        this.robotAngleSupplier = robotAngleSupplier;

        this.aprilTagFilters = new AprilTagFilters(filterConfig, cameraIO, () -> new ChassisSpeeds());
        poseEstimatorSource = new PoseEstimatorSource(() -> getRobotRelaticTwist(poseEstimate, visionTs), () -> xyFom,
                () -> oFom, () -> visionTs);

        PoseEstimator.addSource(poseEstimatorSource);
    }

    public AprilTagCamera(VisionCameraIO cameraIO, FiltersConfig teleopConfig, FiltersConfig autoConfig,
            Supplier<Double> robotAngleSupplier) {
        super(cameraIO);

        this.teleopConfig = teleopConfig;
        this.autoConfig = autoConfig;

        this.robotAngleSupplier = robotAngleSupplier;

        this.aprilTagFilters = new AprilTagFilters(teleopConfig, cameraIO, () -> new ChassisSpeeds());
        poseEstimatorSource = new PoseEstimatorSource(() -> getRobotRelaticTwist(poseEstimate, visionTs), () -> xyFom,
                () -> oFom, () -> visionTs);

        PoseEstimator.addSource(poseEstimatorSource);
    }

    public void setUpdatePoseEstimate(boolean updatePoseEstiamte) {
        this.updatePoseEstiamte = updatePoseEstiamte;
    }

    public FiltersConfig getFiltersConfig() {
        return DriverStation.isTeleop() ? teleopConfig : autoConfig;
    }

    @Override
    public void update() {
        logIO();

        aprilTagFilters.updateFiltersConfig(getFiltersConfig());

        if (updatePoseEstiamte) {
            xyFom = aprilTagFilters.getXyFOM();
            oFom = aprilTagFilters.getOFOM();
            MALog.log("/Subsystems/Vision/Cameras/" + name + "/XY FOM", xyFom);
            MALog.log("/Subsystems/Vision/Cameras/" + name + "/Omega FOM", oFom);

            visionTs = getVisionTimetemp();

            poseEstimatorSource.capture();
        }

    }

    @Override
    protected void logIO() {
        super.logIO();
        poseEstimate = cameraIO.getPoseEstimate(getFiltersConfig().poseEstimateType);

        MALog.log("/Subsystems/Vision/Cameras/" + name + "/Target/Ambiguit", tag.ambiguity);
        MALog.log("/Subsystems/Vision/Cameras/" + name + "/Target/Id", tag.id);

        MALog.log("/Subsystems/Vision/Cameras/" + name + "/Pose Estimate/Pose", poseEstimate.pose);
        MALog.log("/Subsystems/Vision/Cameras/" + name + "/Pose Estimate/Avg Distance", poseEstimate.avgTagDist);
        MALog.log("/Subsystems/Vision/Cameras/" + name + "/Pose Estimate/Tag Count", poseEstimate.tagCount);
        MALog.log("/Subsystems/Vision/Cameras/" + name + "/Pose Estimate/Latency", poseEstimate.latency);
        MALog.log("/Subsystems/Vision/Cameras/" + name + "/Pose Estimate/Timestamp", poseEstimate.timestampSeconds);

    }

    private Double getVisionTimetemp() {
        return Timer.getFPGATimestamp() - (poseEstimate.latency / 1000.0);
    }

    private Twist2d getRobotRelaticTwist(PoseEstimate poseEstimator, double timestemp) {
        visionPose = poseEstimate.pose;
        prior = PoseEstimator.getPoseAt(timestemp);

        delta = new Transform2d(prior, visionPose);

        fieldDx = delta.getTranslation().getX();
        fieldDy = delta.getTranslation().getY();
        fieldDtheta = delta.getRotation().getRadians();

        heading = Rotation2d.fromDegrees(robotAngleSupplier.get());
        robotDx = heading.getCos() * fieldDx
                + heading.getSin() * fieldDy;
        robotDy = -heading.getSin() * fieldDx
                + heading.getCos() * fieldDy;

        visionTwsit.dx = robotDx;
        visionTwsit.dy = robotDy;
        visionTwsit.dtheta = fieldDtheta;

        return visionTwsit;

    }

}
