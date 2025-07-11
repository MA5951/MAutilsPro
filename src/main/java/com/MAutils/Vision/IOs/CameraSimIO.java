
package com.MAutils.Vision.IOs;

import org.photonvision.PhotonCamera;
import org.photonvision.simulation.PhotonCameraSim;
import org.photonvision.simulation.VisionSystemSim;

import com.MAutils.Components.Camera.Cameras;
import com.MAutils.Vision.Util.LimelightHelpers.PoseEstimate;
import com.MAutils.Vision.Util.LimelightHelpers.RawFiducial;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Transform3d;

public class CameraSimIO implements VisionCameraIO {

    private RawFiducial[] blankFiducial = new RawFiducial[] { new RawFiducial(0, 0, 0, 0, 0, 0, 0) };
    private PoseEstimate blankPoseEstimate = new PoseEstimate(new Pose2d(), 0, 0, 0, 0, 0, 0, blankFiducial, false);
    private RawFiducial[] fiducials;
    private PoseEstimate poseEstimate;

    private static VisionSystemSim visionSystemSim;
    private final PhotonCamera camera;
    private final PhotonCameraSim cameraSim;

    public CameraSimIO(String name, Cameras cameraProps, Transform3d robotToCamera) {
        visionSystemSim = new VisionSystemSim("Main Vision Sim");
        
        try {
            AprilTagFieldLayout tagLayout = AprilTagFieldLayout
                .loadFromResource(AprilTagFields.k2025ReefscapeAndyMark.m_resourceFile);//TODO Cheack

                visionSystemSim.addAprilTags(tagLayout);
        } catch (Exception e) {
        }

        camera = new PhotonCamera("name");
        cameraSim = new PhotonCameraSim(camera, cameraProps.getSimulationProp());

        visionSystemSim.addCamera(cameraSim, robotToCamera);

        cameraSim.enableProcessedStream(true);
        cameraSim.enableDrawWireframe(true);
    }

    public void setCameraPosition(Transform3d positionRelaticToRobot) {
        visionSystemSim.adjustCamera(cameraSim, positionRelaticToRobot);
    }

    public void setPipline(int pipeline) {
    }

    public void setCrop(double cropXMin, double cropXMax, double cropYMin, double cropYMax) {
    }

    public void filterTags(int[] tags) {
    }

    public void takeSnapshot() {
    }

    public int getPipline() {
        return 0;
    }

    public RawFiducial getTag() {
        return getFiducials()[0];
    }

    public RawFiducial[] getFiducials() {
        camera.get
        for (RawFiducial rawFiducial : blankFiducial) {
            
        }

    }

    public PoseEstimate getPoseEstimate(PoseEstimateType type) {
    }
}
