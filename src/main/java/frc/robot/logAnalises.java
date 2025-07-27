
package frc.robot;

import java.util.function.Supplier;

import com.MAutils.RobotControl.RobotState;

import edu.wpi.first.wpilibj.Timer;


public class LogAnalises {

    private Timer timeInCurrentStatetimer;

    private RobotState lastRobotState;


    public LogAnalises() {
        lastRobotState = null;

        timeInCurrentStatetimer = new Timer();
        timeInCurrentStatetimer.start();
    }

    public double getTimeInCurrentState(Supplier<RobotState> currentRobotState) {
        
        if (lastRobotState == null) {
            lastRobotState = currentRobotState.get();
        }

        if(currentRobotState.get().equals(lastRobotState)) {
            return timeInCurrentStatetimer.get();
        } else {
            lastRobotState = currentRobotState.get();
            timeInCurrentStatetimer.restart();
            return 0;
        }
    }
}
