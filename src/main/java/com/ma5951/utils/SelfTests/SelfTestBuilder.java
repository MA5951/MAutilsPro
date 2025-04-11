package com.ma5951.utils.SelfTests;

import com.ma5951.utils.Logger.MALog;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import java.util.ArrayList;
import java.util.List;

public class SelfTestBuilder {

    private static final List<Command> testCommands = new ArrayList<>();
    private static String currentSubsystem = "";

    public static SelfTestBuilder start(String subsystemName) {
        testCommands.clear();
        currentSubsystem = subsystemName;
        return new SelfTestBuilder();
    }

    public SelfTestBuilder addTest(String testName, double timeoutSeconds, Runnable testLogic) {
        Command testCommand = new Command() {
            private final Timer timer = new Timer();
            private boolean isDone = false;

            @Override
            public void initialize() {
                String prefix = "SelfTest/" + currentSubsystem;
                MALog.log(prefix + "/Current Test", testName);
                MALog.log(prefix + "/Test Result", "No Results Yet");
                MALog.log(prefix + "/Test Message", "No Message Yet");

                timer.reset();
                timer.start();
            }

            @Override
            public void execute() {
                String prefix = "SelfTest/" + currentSubsystem;
                String testPrefix = prefix + "/" + testName;

                if (timer.hasElapsed(timeoutSeconds)) {
                    MALog.log(testPrefix + "/Status", "ERROR");
                    MALog.log(testPrefix + "/Message", "Timed Out");
                    MALog.log(prefix + "/Test Result", "ERROR");
                    MALog.log(prefix + "/Test Message", "Timed Out");
                    isDone = true;
                    return;
                }

                SelfTestResult result = new SelfTestResult(null, testName); //= testLogic.run(); //TODO: Fix this line
                if (result != null) {
                    MALog.log(testPrefix + "/Status", result.getStatus().name());
                    MALog.log(testPrefix + "/Message", result.getMessage());
                    MALog.log(prefix + "/Test Result", result.getStatus().name());
                    MALog.log(prefix + "/Test Message", result.getMessage());
                    isDone = true;
                }
            }

            @Override
            public boolean isFinished() {
                return isDone;
            }
        };

        testCommands.add(testCommand.withName("Test: " + testName));
        return this;
    }

    public Command build() {
        return new SequentialCommandGroup(testCommands.toArray(new Command[0]));
    }
}
