package frc.robot;



import com.MAutils.Controllers.XboxMAController;
import com.MAutils.RobotControl.DeafultRobotContainer;
import com.MAutils.RobotControl.StateTrigger;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Commands.IntakeCommand;
import frc.robot.Commands.Swerve.SwerveTeleopController;
import frc.robot.Subsystems.Intake.Intake;
import frc.robot.Subsystems.Swerve.Swerve;
import frc.robot.Subsystems.Swerve.SwerveConstants;

public class RobotContainer extends DeafultRobotContainer{


  @SuppressWarnings("static-access")
  public RobotContainer() {
    super();

    setGamePiecesList(new String[] {
      "Coral",
      "Algae"
    });  
      
    setDriverController(new XboxMAController(0));
    setSwerveDriveSimulation(SwerveConstants.SWERVE_CONSTANTS.SWERVE_DRIVE_SIMULATION);

    addSystemCommand(new IntakeCommand());

    Swerve.getInstance();

    CommandScheduler.getInstance().setDefaultCommand(
      Swerve.getInstance(),
      new SwerveTeleopController()
    );


    configureBindings();

    RobotConstatns.INTAKE_IDLE.registerSubsystes(Intake.getInstance());
    RobotConstatns.INTAKE_FORWARD.registerSubsystes(Intake.getInstance());
    RobotConstatns.INTAKE_REVERSE.registerSubsystes(Intake.getInstance());
  }

  private void configureBindings() {

    T(StateTrigger.T(() -> driverController.getActionsDown(), RobotConstatns.INTAKE_IDLE));

    T(StateTrigger.T(() -> driverController.getActionsLeft(), RobotConstatns.INTAKE_FORWARD));

    T(StateTrigger.T(() -> driverController.getActionsUp(), RobotConstatns.INTAKE_REVERSE));

  }

}
