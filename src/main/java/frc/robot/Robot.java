
package frc.robot;

import org.ironmaple.simulation.SimulatedArena;

import com.ma5951.utils.Controllers.MAPS5Controller;
import com.ma5951.utils.DashBoard.DashboardPID;
import com.ma5951.utils.Logger.MALog;
import com.ma5951.utils.Logger.MALog.MALogMode;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.PS5Controller.Button;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Subsystem.PoseEstimation.PoseEstimator;
import frc.robot.Subsystem.Swerve.SwerveConstants;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;
  public static boolean isStartingPose = false;
  private MAPS5Controller controller;

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();

    controller = new MAPS5Controller(0);

  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
    PoseEstimator.getInstance().update();
    m_robotContainer.updatePeriodic();


  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    m_robotContainer.updateDisablePeriodic();
  }

  @Override
  public void autonomousInit() {
    m_robotContainer.updateAutoInit();
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void simulationInit() {
    SimulatedArena.getInstance().addDriveTrainSimulation(SwerveConstants.SWERVE_DRIVE_SIMULATION);
  }

  @Override
  public void simulationPeriodic() {
    SimulatedArena.getInstance().simulationPeriodic();
    // LOG SWERVE POSE
  }

}
