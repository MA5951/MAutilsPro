
package frc.robot.Commands;

import com.MAutils.Swerve.SwerveSystemController;

import frc.robot.RobotContainer;
import frc.robot.Subsystems.Swerve.SwerveConstants;

public class SwerveTeleopController extends SwerveSystemController{

    public SwerveTeleopController() {
        super(RobotContainer.swerve, SwerveConstants.SWERVE_CONSTANTS, RobotContainer.getDriverController());
    }

    public void ConfigControllers() {
    }

    public void SetSwerveState() {
        setState(SwerveConstants.FIELD_DRIVE);
    }
}
