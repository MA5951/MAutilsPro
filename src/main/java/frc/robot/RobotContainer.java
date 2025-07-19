package frc.robot;



import com.MAutils.RobotControl.DeafultRobotContainer;
import com.MAutils.Swerve.SwerveSystem;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Commands.SwerveTeleopController;
import frc.robot.Subsystems.Swerve.SwerveConstants;

public class RobotContainer extends DeafultRobotContainer{

  public static final SwerveSystem swerve = SwerveSystem.getInstance(SwerveConstants.SWERVE_CONSTANTS);

  public RobotContainer() {
    super();

    CommandScheduler.getInstance().setDefaultCommand(swerve, new SwerveTeleopController());
  }


}
