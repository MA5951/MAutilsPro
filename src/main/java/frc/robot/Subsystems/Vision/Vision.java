
package frc.robot.Subsystems.Vision;

import com.MAutils.Vision.VisionSystem;

public class Vision {

    public Vision() {
        VisionSystem.getInstance()
        .setCameras(VisionConstants.frontLeftLimelight);;
    }


}
