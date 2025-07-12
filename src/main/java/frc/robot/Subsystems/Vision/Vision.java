
package frc.robot.Subsystems.Vision;

import com.MAutils.Swerve.SwerveSystem;
import com.MAutils.Vision.VisionSystem;

import frc.robot.PortMap.Swerve;

public class Vision extends VisionSystem {
    private static Vision instance;

    public static Vision getInstance() {
        if (instance == null) {
            instance = new Vision();
        }
        return instance;
    }

    private Vision() {
        super();
    }

    @Override
    public void periodic() {
        super.periodic();
    }



}
