
package com.MAutils.Vision.IOs;

import static edu.wpi.first.units.Units.derive;

import java.util.function.Supplier;

import com.MAutils.Logger.MALog;
import com.MAutils.PoseEstimation.PoseEstimator;
import com.MAutils.PoseEstimation.PoseEstimatorSource;
import com.MAutils.Utils.ConvUtil;
import com.MAutils.Vision.Filters.AprilTagFilters;
import com.MAutils.Vision.Filters.FiltersConfig;
import com.MAutils.Vision.Util.LimelightHelpers.PoseEstimate;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

public class AprilTagCamera extends Camera {

    private FiltersConfig teleopConfig;
    private FiltersConfig autoConfig;
    private AprilTagFilters aprilTagFilters;

    private boolean updatePoseEstiamte = true;
    private PoseEstimate poseEstimate;
    private double fom;

    private final PoseEstimatorSource poseEstimatorSource;

    public final Supplier<Double> robotAngleSupplier;

    public AprilTagCamera(VisionCameraIO cameraIO, FiltersConfig filterConfig, Supplier<Double> robotAngleSupplier) {
        super(cameraIO);

        this.teleopConfig = filterConfig;
        this.autoConfig = filterConfig;

        this.robotAngleSupplier = robotAngleSupplier;

        this.aprilTagFilters = new AprilTagFilters(filterConfig, cameraIO, () -> new ChassisSpeeds());
        poseEstimatorSource = new PoseEstimatorSource();

        PoseEstimator.addSource(poseEstimatorSource);
    }

    public AprilTagCamera(VisionCameraIO cameraIO, FiltersConfig teleopConfig, FiltersConfig autoConfig,
            Supplier<Double> robotAngleSupplier) {
        super(cameraIO);

        this.teleopConfig = teleopConfig;
        this.autoConfig = autoConfig;

        this.robotAngleSupplier = robotAngleSupplier;

        this.aprilTagFilters = new AprilTagFilters(teleopConfig, cameraIO, () -> new ChassisSpeeds());
        poseEstimatorSource = new PoseEstimatorSource();

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
            fom = aprilTagFilters.getFOM();
            MALog.log("/Subsystems/Vision/Cameras/" + name + "/FOM", fom);

            double visionTs = getVisionTimetemp();

            poseEstimatorSource.addMeasurement(
                    getRobotRelaticTwist(poseEstimate, visionTs),
                    fom,
                    visionTs);
        }

        // TODO: Add update heading
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
        Pose2d visionPose = poseEstimate.pose;

        Pose2d prior = PoseEstimator.getPoseAt(timestemp);

        Transform2d delta = new Transform2d(prior, visionPose);

        double fieldDx = delta.getTranslation().getX();
        double fieldDy = delta.getTranslation().getY();
        double fieldDtheta = delta.getRotation().getRadians();

        Rotation2d heading = Rotation2d.fromDegrees(robotAngleSupplier.get());
        double robotDx = heading.getCos() * fieldDx
                + heading.getSin() * fieldDy;
        double robotDy = -heading.getSin() * fieldDx
                + heading.getCos() * fieldDy;

        return new Twist2d(robotDx, robotDy, fieldDtheta);

    }

}
