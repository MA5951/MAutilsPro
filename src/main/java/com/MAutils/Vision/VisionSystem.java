
package com.MAutils.Vision;

import com.MAutils.Swerve.SwerveSystem;
import com.MAutils.Vision.IOs.VisionCamera;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class VisionSystem extends SubsystemBase{

    private VisionCamera[] cameras;
    private SwerveSystem swerveSystem;

    public VisionSystem(SwerveSystem swerveSystem, VisionCamera... cameras) {
        this.swerveSystem = swerveSystem;
        this.cameras = cameras;
    }


    @Override
    public void periodic() {
        
    }


}
