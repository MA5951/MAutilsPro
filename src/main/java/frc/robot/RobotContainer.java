package frc.robot;


import com.MAutils.Controllers.XboxMAController;
import com.MAutils.RobotControl.DeafultRobotContainer;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Commands.Arm.ArmCommand;
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

public Command getAutonomousCommand() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAutonomousCommand'");
}


}
