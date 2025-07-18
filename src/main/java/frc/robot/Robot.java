
package frc.robot;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import com.MAutils.CanBus.StatusSignalsRunner;
import com.MAutils.Simulation.SimulationManager;

import edu.wpi.first.util.datalog.DataLogIterator;
import edu.wpi.first.util.datalog.DataLogReader;
import edu.wpi.first.util.datalog.DataLogRecord;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;

  private DataLogReader dataLogReader;
  private DataLogIterator dataLogIterator;
  private DataLogRecord dataLogRecord;


  //TODO what the point?
  public String findReplayLogAdvantageScope() {
    Path advantageScopeTempPath =
        Paths.get(System.getProperty("java.io.tmpdir"), "akit-log-path.txt");
    String advantageScopeLogPath = null;
    try (Scanner fileScanner = new Scanner(advantageScopeTempPath)) {
      advantageScopeLogPath = fileScanner.nextLine();
    } catch (IOException e) {
    }
    return advantageScopeLogPath;
  }

  public Robot() {
    m_robotContainer = new RobotContainer();
    

    try {
      dataLogReader = new DataLogReader(findReplayLogAdvantageScope());
    } catch (Exception e) {
      System.out.println(e);
    }


    


    
  }

  @Override
  public void robotPeriodic() {
    StatusSignalsRunner.refreshAll();
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    SimulationManager.autoInit();
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
    SimulationManager.simulationInit();
  }

  @Override
  public void simulationPeriodic() {
    SimulationManager.updateSimulation();
  }

}
