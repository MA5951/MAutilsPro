
package com.MAutils.Subsystems.SelfTests;

import java.util.ArrayList;
import java.util.List;

import com.MAutils.Logger.MALog;
import com.MAutils.RobotControl.StateSubsystem;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class SelfSystemTest{

    private List<Test> testsList = new ArrayList<>();
    private String logTable;
    private SequentialCommandGroup commandGroup;

    public SelfSystemTest(StateSubsystem  subsystem) {
        super();
        logTable = "/Subsystem/" + subsystem.subsystemName + "/SelfTest/" + "/Status";
        commandGroup.addRequirements(subsystem);
    }

    public SelfSystemTest addTest(Test test) {
        testsList.add(test);
        return this;
    }

    private void runTest(Test test) {
        commandGroup.addCommands(

        new SequentialCommandGroup(
            new InstantCommand(() -> MALog.log(logTable, "Runing test: " + test.testName))),
            new ParallelRaceGroup(
                new SequentialCommandGroup(
                    new WaitUntilCommand(test.testTimeCap),
                    new InstantCommand(() -> MALog.log(logTable, "Test timed out: " + test.testName))
                ),
                new ParallelDeadlineGroup(
                    new SequentialCommandGroup(
                        new WaitUntilCommand(test.testCondition),
                        new InstantCommand(() -> MALog.log(logTable, "Test passed: " + test.testName))
                    ),
                    new InstantCommand(test.testAction).repeatedly())
            ),
            new InstantCommand(() -> MALog.log(logTable, "Test Finished: " + test.testName))
        );
    }

    public Command runTests() {
        for (Test test : testsList) {
            runTest(test);
        }
        return commandGroup;
    }

}
