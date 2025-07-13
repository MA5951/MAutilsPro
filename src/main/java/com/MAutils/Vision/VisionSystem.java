
package com.MAutils.Vision;

import com.MAutils.Vision.IOs.VisionCameraIO;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class VisionSystem extends SubsystemBase{

    private VisionCameraIO[] cameras;

    public VisionSystem(VisionCameraIO... cameras) {
        this.cameras = cameras;
    }


    @Override
    public void periodic() {
        for (VisionCameraIO visionCameraIO : cameras) {
        }
    }


}
