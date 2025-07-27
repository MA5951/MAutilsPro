
package frc.robot;

import java.util.function.Supplier;

import com.MAutils.RobotControl.RobotState;

import edu.wpi.first.wpilibj.Timer;


public class logAnalises {

    private Timer timer;

    private RobotState lastRobotState;


    public logAnalises() {
        lastRobotState = null;

        timer = new Timer();
        timer.start();
    }

    public double getTimeInCurrentState(Supplier<RobotState> currentRobotState) {
        if(lastRobotState == null || currentRobotState.get().equals(lastRobotState)) {
            return timer.get();
        } else {
            lastRobotState = currentRobotState.get();
            timer.restart();
            return 0;
        }
    }
}
