
package frc.robot.Commands.Swerve;

import com.MAutils.Swerve.SwerveSystemController;
import com.MAutils.Swerve.Utils.SwerveState;

import frc.robot.RobotContainer;
import frc.robot.Subsystems.Swerve.Swerve;
import frc.robot.Subsystems.Swerve.SwerveConstants;

public class SwerveTeleopController extends SwerveSystemController{

    private final SwerveState TRAVEL_STATE = new SwerveState("Travel");

    public SwerveTeleopController() {
        super(Swerve.getInstance(), SwerveConstants.SWERVE_CONSTANTS, RobotContainer.getDriverController());
    }

    public void ConfigControllers() {
        TRAVEL_STATE.withSpeeds(fieldCentricDriveController);
    }

    public void SetSwerveState() {
        setState(TRAVEL_STATE);
    }
}
