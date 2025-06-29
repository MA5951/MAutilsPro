package frc.robot;


import com.MAutils.Controllers.XboxMAController;
import com.MAutils.RobotControl.DeafultRobotContainer;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Commands.Arm.ArmCommand;
import frc.robot.Commands.Intake.IntakeCommand;
import frc.robot.Commands.Shooter.ShooterCommand;
import frc.robot.Commands.Swerve.SwerveTeleopController;
import frc.robot.Subsystems.Swerve.Swerve;
import frc.robot.Subsystems.Swerve.SwerveConstants;

public class RobotContainer extends DeafultRobotContainer{


  public RobotContainer() {
    super();

    setGamePiecesList(new String[] {
      "Coral",
      "Algae"
    });  
      
    setDriverController(new XboxMAController(0));
    setSwerveDriveSimulation(SwerveConstants.SWERVE_CONSTANTS.SWERVE_DRIVE_SIMULATION);

    addSystemCommand(new ArmCommand());
    addSystemCommand(new ShooterCommand());
    addSystemCommand(new IntakeCommand());

    Swerve.getInstance();

    CommandScheduler.getInstance().setDefaultCommand(
      Swerve.getInstance(),
      new SwerveTeleopController()
    );


    configureBindings();
  }

  private void configureBindings() {

    T(() -> driverController.getActionsDown(),  RobotConstatns.ARM_IDLE).build();

    T(() -> driverController.getActionsRight(),  RobotConstatns.ARM_DOWN).build();

    T(() -> driverController.getActionsUp(),  RobotConstatns.ARM_UP).build();



  }

}
