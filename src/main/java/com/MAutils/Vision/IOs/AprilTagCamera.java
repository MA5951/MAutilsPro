
package com.MAutils.Vision.IOs;

import com.MAutils.Logger.MALog;
import com.MAutils.PoseEstimation.PoseEstimatorSource;
import com.MAutils.Vision.Filters.AprilTagFilters;
import com.MAutils.Vision.Filters.FiltersConfig;
import com.MAutils.Vision.Util.LimelightHelpers.PoseEstimate;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;

public class AprilTagCamera extends Camera {

    private FiltersConfig teleopConfig;
    private FiltersConfig autoConfig;
    private AprilTagFilters aprilTagFilters;

    private boolean updatePoseEstiamte = true;
    private PoseEstimate poseEstimate;
    private double fom;

    //private final PoseEstimatorSource poseEstimatorSource;


    public AprilTagCamera(VisionCameraIO cameraIO, FiltersConfig filterConfig) {
        super(cameraIO);

        this.teleopConfig = filterConfig;
        this.autoConfig = filterConfig;

        this.aprilTagFilters = new AprilTagFilters(filterConfig, cameraIO, () -> new ChassisSpeeds());
        //poseEstimatorSource = new PoseEstimatorSource(aprilTagFilters::getFOM);
    }

    public AprilTagCamera(VisionCameraIO cameraIO, FiltersConfig teleopConfig, FiltersConfig autoConfig) {
        super(cameraIO);

        this.teleopConfig = teleopConfig;
        this.autoConfig = autoConfig;

        this.aprilTagFilters = new AprilTagFilters(teleopConfig, cameraIO, () -> new ChassisSpeeds());
        //poseEstimatorSource = new PoseEstimatorSource(aprilTagFilters::getFOM);
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
            // poseEstimatorSource.sendDataLatency(
            //         // FOMPoseEstimator.getPoseAtTime(poseEstimate.timestampSeconds).log(poseEstimate.pose),
            //         // poseEstimate.timestampSeconds);
        }

        // Add update heading
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

}
