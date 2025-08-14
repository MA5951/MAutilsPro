
package frc.robot.LogAnalises;

import java.util.ArrayList;
import java.util.function.Supplier;

import com.MAutils.Logger.MALog;
import com.MAutils.RobotControl.RobotState;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.PowerSystemIO;
import com.MAutils.Subsystems.DeafultSubsystems.Systems.PowerControlledSystem;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.Timer;

public class LogAnalises {

    private int numOfIterations = 1;

    private class CurrentEntry {
        public final String subsystemName;
        public final double currentPrecent;
        
        public CurrentEntry(String name, double precent) {
            subsystemName = name;
            currentPrecent = precent;
        }
    }

    private Timer timeInCurrentStatetimer;
    private Timer timebetweenStatestimer;
    private Timer timeInMaxSpeed;

    private RobotState lastRobotState;
    private Supplier<RobotState> currentRobotState;
    private RobotState firstRobotState;
    private RobotState secondRobotState;

    private double maxVelocity;
    private Supplier<Double> currentVelocity;

    private double maxVelocityTime;
    private PowerControlledSystem[] subsystems; 
    private double[] systemCurrents;
    private CurrentEntry[] currentPrecents;

    private PowerDistribution PDH;

    private double sumOfCurrents;
    private double sumOfVoltage;

    public LogAnalises(Supplier<RobotState> currentRobotState,
     RobotState firstRobotState, RobotState secondRobotState,
     Supplier<Double> currentVelocity, PowerControlledSystem... Subsystems) {

        lastRobotState = null;

        this.currentRobotState = currentRobotState;
        this.firstRobotState = firstRobotState;
        this.secondRobotState = secondRobotState;

        this.subsystems = Subsystems;
        systemCurrents = new double[subsystems.length];
        currentPrecents = new CurrentEntry[subsystems.length];
        

        timeInCurrentStatetimer = new Timer();
        timeInCurrentStatetimer.start();

        timebetweenStatestimer = new Timer();

        timeInMaxSpeed = new Timer();

        this.currentVelocity = currentVelocity;
        maxVelocity = 0;
        maxVelocityTime = 0;

        systemNum = 0;

        sumOfCurrents = 0;
        sumOfVoltage = 0;
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

    public double getCycleTime(boolean startingCondition, boolean endingCondition) {
        if ((currentRobotState.get().equals(firstRobotState)) && startingCondition) {
            timebetweenStatestimer.start();
        } else if(currentRobotState.get().equals(secondRobotState) && endingCondition) {
            return timebetweenStatestimer.get();
        }
        return -1;
    }

    private void findingMaxVelocityAndWhoLongIsIt() {
        if (currentVelocity.get() > maxVelocity) {
            maxVelocity = currentVelocity.get();
        }
        if (Math.abs(currentVelocity.get()- maxVelocity) > LogAnalisesConstants.VELOCITY_TOLERANC) {
            timeInMaxSpeed.start();
            maxVelocityTime = timeInMaxSpeed.get();
        } else {
            timeInMaxSpeed.reset();
        }
    }

    public double getMaxVelocity() {
        findingMaxVelocityAndWhoLongIsIt();
        return maxVelocity;
    }

    public double getMaxVelocityTime() {
        findingMaxVelocityAndWhoLongIsIt();
        return maxVelocityTime;
    }

    public void runCurrent() {
        numOfIterations ++;

        for (int i = 0; i < subsystems.length - 1 ; i++) {
            systemCurrents[i] += subsystems[i].getCurrent();
            currentPrecents[i] = new CurrentEntry(subsystems[i].getName(), systemCurrents[i] / sumOfCurrents * 100);
        }
    }

    public double getTotalCurrent() {
        sumOfCurrents += PDH.getTotalCurrent();
        return sumOfCurrents;
    }

    public CurrentEntry[] getCurrentPrecent() {
        

        return currentPrecents;
    }

}
