
package frc.robot.Subsystems.Swerve;

import com.MAutils.Swerve.SwerveSystem;

import frc.robot.Subsystems.PoseEstimator.PoseEstimator;

public class Swerve extends SwerveSystem{
    private static Swerve instance;

    public Swerve() {
        super(SwerveConstants.SWERVE_CONSTANTS, PoseEstimator.getInstance());
    }

    public static Swerve getInstance() {
        if (instance == null) {
            instance = new Swerve();
        }
        return instance;
    }

}
