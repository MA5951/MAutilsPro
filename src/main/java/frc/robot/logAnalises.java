
package frc.robot;

import java.util.function.Supplier;

import com.MAutils.RobotControl.RobotState;

import edu.wpi.first.wpilibj.Timer;


public class LogAnalises {

    private Timer timeInCurrentStatetimer;

    private RobotState lastRobotState;
    private RobotState currentRobotState;



    public LogAnalises(Supplier<RobotState> currentRobotState) {
        lastRobotState = null;

        this.currentRobotState = currentRobotState.get();

        timeInCurrentStatetimer = new Timer();
        timeInCurrentStatetimer.start();
    }

    public double getTimeInCurrentState() {
        
        if (lastRobotState == null) {
            lastRobotState = currentRobotState;
        }

        if(currentRobotState.equals(lastRobotState)) {
            return timeInCurrentStatetimer.get();
        } else {
            lastRobotState = currentRobotState;
            timeInCurrentStatetimer.restart();
            return 0;
        }
    }
}
