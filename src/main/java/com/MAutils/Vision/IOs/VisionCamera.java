package com.MAutils.Vision.IOs;

import edu.wpi.first.math.geometry.Pose3d;

public class VisionCamera {
    public final String name;
    private final VisionCameraIO io;
    private Pose3d positionRelaticToRobot;

    public VisionCamera(String name, Pose3d positionRelaticToRobot,VisionCameraIO io) {
        this.name = name;
        this.io = io;
        this.positionRelaticToRobot = positionRelaticToRobot;
    }


    public void setPositionRelaticToRobot(Pose3d positionRelaticToRobot) {
        this.positionRelaticToRobot = positionRelaticToRobot;
    }

    public void update() {
    }


}
