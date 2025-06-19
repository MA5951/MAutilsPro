package frc.robot;


import com.MAutils.RobotControl.DeafultRobotContainer;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Subsystems.Swerve.Swerve;
import frc.robot.Subsystems.Swerve.SwerveConstants;

public class RobotContainer extends DeafultRobotContainer{


  public RobotContainer() {
    super();

    setGamePiecesList(new String[] {
      "Coral",
      "Algae"
    });  
      
    setSwerveDriveSimulation(SwerveConstants.SWERVE_CONSTANTS.SWERVE_DRIVE_SIMULATION);

    Swerve.getInstance();

    CommandScheduler.getInstance().setDefaultCommand(
      Swerve.getInstance(),
      new frc.robot.Commands.Swerve.SwerveTeleopController()
    );


    configureBindings();
  }

  private void configureBindings() {}

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
