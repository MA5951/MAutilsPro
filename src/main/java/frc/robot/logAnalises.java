
package frc.robot;

import java.util.function.Supplier;

import com.MAutils.RobotControl.RobotState;

import edu.wpi.first.wpilibj.Timer;


public class LogAnalises {

    private Timer timeInCurrentStatetimer;
    private Timer timebetweenStatestimer;


    private RobotState lastRobotState;
    private Supplier<RobotState> currentRobotState;
    private Supplier<RobotState> firstRobotState;
    private Supplier<RobotState> secondRobotState;


    public LogAnalises(Supplier<RobotState> currentRobotState,
     Supplier<RobotState> firstRobotState, Supplier<RobotState> secondRobotState) {
        lastRobotState = null;

        this.currentRobotState = currentRobotState;
        this.firstRobotState = firstRobotState;
        this.secondRobotState = secondRobotState;

        timeInCurrentStatetimer = new Timer();
        timeInCurrentStatetimer.start();

        timebetweenStatestimer = new Timer();
        timebetweenStatestimer.start();

    }

    public double getTimeInCurrentState() {
        
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

    public double gettimebetweenStates() {
        if (currentRobotState.get().equals(firstRobotState.get())) {
            timebetweenStatestimer.restart();
        } else if(currentRobotState.get().equals(secondRobotState.get())) {
            return timebetweenStatestimer.get();
        }
        return -1;
    }
}
