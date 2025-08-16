
package com.MAutils.Vision.IOs;


import java.util.function.Supplier;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.simulation.PhotonCameraSim;
import org.photonvision.simulation.VisionSystemSim;
import org.photonvision.targeting.PhotonTrackedTarget;

import com.MAutils.Components.CameraTypes.Cameras;
import com.MAutils.Simulation.SimulationManager;
import com.MAutils.Simulation.Simulatables.VisionWorldSimulation;
import com.MAutils.Vision.Util.VisionUtil;
import com.MAutils.Vision.Util.LimelightHelpers.PoseEstimate;
import com.MAutils.Vision.Util.LimelightHelpers.RawFiducial;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;

public class CameraSimIO implements VisionCameraIO {

    private RawFiducial[] blankFiducial = new RawFiducial[] { new RawFiducial(0, 0, 0, 0, 0, 0, 0) };
    private PoseEstimate blankPoseEstimate = new PoseEstimate(new Pose2d(-1 , -1, new Rotation2d()), 0, 0, 0, 0, 0, 0, blankFiducial, false);
    private RawFiducial[] fiducials;
    private PoseEstimate poseEstimate;
    private Transform3d robotToCamera;
    private AprilTagFieldLayout tagLayout;
    private PhotonPoseEstimator poseEstimator;
    private EstimatedRobotPose photonPoseEstimate;
    private int i = 0;

    public static VisionSystemSim visionSystemSim;
    private static boolean isRegisterd = false;
    private final PhotonCamera camera;
    private final PhotonCameraSim cameraSim;

    public CameraSimIO(String name, Cameras cameraProps, Transform3d robotToCamera, Supplier<Pose2d> robotPose) {
        this.robotToCamera = robotToCamera;
        visionSystemSim = new VisionSystemSim("Main Vision Sim");

        try {
            tagLayout = AprilTagFieldLayout
                    .loadFromResource(AprilTagFields.k2025ReefscapeAndyMark.m_resourceFile);// TODO Cheack

            visionSystemSim.addAprilTags(tagLayout);
        } catch (Exception e) {
        }

        camera = new PhotonCamera(name);
        cameraSim = new PhotonCameraSim(camera, cameraProps.getSimulationProp());

        poseEstimator = new PhotonPoseEstimator(tagLayout, PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, robotToCamera);

        visionSystemSim.addCamera(cameraSim, robotToCamera);

        cameraSim.enableProcessedStream(true);
        cameraSim.enableDrawWireframe(true);

        if (!isRegisterd) {
            SimulationManager.registerSimulatable(new VisionWorldSimulation(robotPose));
            isRegisterd = true;
        }
    }

    public void setCameraPosition(Transform3d positionRelaticToRobot) {
        visionSystemSim.adjustCamera(cameraSim, positionRelaticToRobot);
    }

    public void setPipline(int pipeline) {
    }

    public void setCrop(double cropXMin, double cropXMax, double cropYMin, double cropYMax) {
    }

    public void allowTags(int[] tags) {
    }

    public void takeSnapshot() {
    }

    public int getPipline() {
        return 0;
    }

    public boolean isTag() {
        return camera.getAllUnreadResults().get(0).hasTargets();
    }

    public RawFiducial getTag() {
        return getFiducials()[0];
    }

    public RawFiducial[] getFiducials() {
        if (camera.getAllUnreadResults().get(0) != null && camera.getAllUnreadResults().get(0).targets.size() > 0) {
            fiducials = new RawFiducial[camera.getAllUnreadResults().get(0).targets.size()];
            i = 0;
            for (PhotonTrackedTarget tracketTrTarget : camera.getAllUnreadResults().get(0).targets) {
                fiducials[i].txnc = tracketTrTarget.yaw;
                fiducials[i].tync = tracketTrTarget.pitch;
                fiducials[i].ambiguity = tracketTrTarget.poseAmbiguity;
                fiducials[i].id = tracketTrTarget.fiducialId;
                fiducials[i].ta = tracketTrTarget.area;
                fiducials[i].distToCamera = VisionUtil.getDistance(robotToCamera,
                        tagLayout.getTagPose(fiducials[i].id).get().getZ(), fiducials[i].tync);
                fiducials[i].distToRobot = VisionUtil.getDistance(robotToCamera,
                        tagLayout.getTagPose(fiducials[i].id).get().getZ(), fiducials[i].tync);// TODO calculate

                i++;
            }
        }

        return blankFiducial;
    }

    public PoseEstimate getPoseEstimate(PoseEstimateType type) {
        if (camera.getAllUnreadResults().get(0) != null) {
            photonPoseEstimate = poseEstimator.update(camera.getAllUnreadResults().get(0)).get();
            poseEstimate.pose = photonPoseEstimate.estimatedPose.toPose2d();
            poseEstimate.timestampSeconds = photonPoseEstimate.timestampSeconds;
            poseEstimate.isMegaTag2 = true;
            poseEstimate.rawFiducials = getFiducials();
            poseEstimate.tagCount = getFiducials().length;
            
            return poseEstimate;
        }

        return blankPoseEstimate;
    }

    @Override
    public String getName() {
        return camera.getName();
    }

    public void update() {
    }

}
