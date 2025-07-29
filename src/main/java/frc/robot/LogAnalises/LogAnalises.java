
package frc.robot.LogAnalises;

import java.util.function.Supplier;

import com.MAutils.RobotControl.RobotState;

import edu.wpi.first.wpilibj.Timer;


public class LogAnalises {

    private Timer timeInCurrentStatetimer;
    private Timer timebetweenStatestimer;
    private Timer timeInMaxSpeed;


    private RobotState lastRobotState;
    private Supplier<RobotState> currentRobotState;
    private Supplier<RobotState> firstRobotState;
    private Supplier<RobotState> secondRobotState;


    private double lastVelocity;
    private Supplier<Double> currentVelocity;

    private double maxVelocityTime;


    public LogAnalises(Supplier<RobotState> currentRobotState,
     Supplier<RobotState> firstRobotState, Supplier<RobotState> secondRobotState, Supplier<Double> currentVelocity) {
        lastRobotState = null;

        this.currentRobotState = currentRobotState;
        this.firstRobotState = firstRobotState;
        this.secondRobotState = secondRobotState;

        timeInCurrentStatetimer = new Timer();
        timeInCurrentStatetimer.start();

        timebetweenStatestimer = new Timer();

        timeInMaxSpeed = new Timer();

        this.currentVelocity = currentVelocity;
        lastVelocity = 0;
        maxVelocityTime = 0;

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

    public double getSycleTime(boolean startingCondition, boolean endingCondition) {
        if ((currentRobotState.get().equals(firstRobotState.get())) && startingCondition) {
            timebetweenStatestimer.start();
        } else if(currentRobotState.get().equals(secondRobotState.get()) && endingCondition) {
            return timebetweenStatestimer.get();
        }
        return -1;
    }

    private void findingMaxVelocityAndWhoLongIsIt() {
        if (currentVelocity.get() > lastVelocity) {
            lastVelocity = currentVelocity.get();
        }
        if (Math.abs(currentVelocity.get()- lastVelocity) > LogAnalisesConstants.VELOCITY_TOLERANC) {
            timeInMaxSpeed.start();
            maxVelocityTime = timeInMaxSpeed.get();
        } else {
            timeInMaxSpeed.reset();
        }
    }

    public double getMaxVelocity() {
        findingMaxVelocityAndWhoLongIsIt();
        return lastVelocity;
    }

    public double getMaxVelocityTime() {
        findingMaxVelocityAndWhoLongIsIt();
        return maxVelocityTime;
    }

}
