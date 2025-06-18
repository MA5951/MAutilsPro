
package frc.robot;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Subsystems.Swerve.Swerve;
import frc.robot.Subsystems.Swerve.SwerveConstants;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;

  private Test test;
  private Test test2;


  public Robot() {
    m_robotContainer = new RobotContainer();

    test = new Test();
    test2 = new Test();
    test.TestInit(7, 1);
    test2.TestInit(5, 4);

    

  }

  @Override
  public void robotPeriodic() {
    RobotContainer.robotPeriodic();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {
    ChassisSpeeds speeds = new ChassisSpeeds(
      -RobotContainer.getDriverController().getLeftY() * SwerveConstants.SWERVE_CONSTANTS.MAX_VELOCITY, 
      -RobotContainer.getDriverController().getLeftX() * SwerveConstants.SWERVE_CONSTANTS.MAX_VELOCITY, 
      -RobotContainer.getDriverController().getRightX() * SwerveConstants.SWERVE_CONSTANTS.MAX_ANGULAR_VELOCITY);

    Swerve.getInstance().drive(speeds);
  }

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}

  @Override
  public void simulationInit() {
    RobotContainer.simulationInit(true);
  }

  @Override
  public void simulationPeriodic() {
    RobotContainer.simulationPeriodic();
  }
}
