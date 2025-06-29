
package frc.robot.Subsystems.PoseEstimator;

import com.MAutils.PoseEstimation.DeafultPoseEstimator;

import frc.robot.Subsystems.Swerve.SwerveConstants;

public class PoseEstimator extends DeafultPoseEstimator{
    private static PoseEstimator instance;  //Todo delete 

    public PoseEstimator() {
        super(SwerveConstants.SWERVE_CONSTANTS);
    }


    public static PoseEstimator getInstance() {
        if (instance == null) {
            instance = new PoseEstimator();
        }
        return instance;
    }

}
