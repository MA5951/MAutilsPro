package frc.robot;



import com.MAutils.RobotControl.DeafultRobotContainer;
import com.MAutils.Swerve.SwerveSystem;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Commands.SwerveTeleopController;
import frc.robot.Subsystems.Swerve.SwerveConstants;
import frc.robot.Subsystems.Vision.Vision;

public class RobotContainer extends DeafultRobotContainer{

  public static final SwerveSystem swerve = SwerveSystem.getInstance(SwerveConstants.SWERVE_CONSTANTS);

  public RobotContainer() {
    super();
    new Vision();

    CommandScheduler.getInstance().setDefaultCommand(swerve, new SwerveTeleopController());
  }


}
