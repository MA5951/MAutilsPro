
package frc.robot.LogAnalises;

import java.util.function.Supplier;

import com.MAutils.Logger.MALog;
import com.MAutils.RobotControl.RobotState;
import com.MAutils.Subsystems.DeafultSubsystems.Systems.PowerControlledSystem;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.Timer;

public class LogAnalysis {

    private static class MeasureEntry {
        public MeasureEntry(String name, double precent) {}
    }

    private static final Timer timeInCurrentStatetimer = new Timer();
    private static final Timer timebetweenStatestimer = new Timer();
    private static final Timer timeInMaxSpeed = new Timer();
    private static final Timer autoTimer= new Timer();

    private static RobotState lastRobotState;
    private static RobotState firstRobotState;
    private static RobotState secondRobotState;

    private static Supplier<RobotState> currentRobotState;

    private static int cyclaeNum;

    private static double maxVelocity;
    private static double maxVelocityTime;
    private static double sumOfCurrents;
    private static double sumOfVoltage;
    private static double autoTime;
    private static double minCycleTime;
    private static double maxCycleTime;
    private static double allCycles;

    private static Supplier<Double> currentVelocity;

    private static double[] systemCurrents;
    private static double[] systemVoltage;
    private static PowerControlledSystem[] subsystems;
    private static MeasureEntry[] currentPrecents;
    private static MeasureEntry[] voltagePrecents;


    private static final PowerDistribution PDH = new PowerDistribution();

    private static boolean logCycleTimes;
    private static boolean logTimeCurrentState;
    private static boolean logMaxVelocity;
    private static boolean logCurrent;
    private static boolean logIsAuto;
    private static boolean logVoltage;

    public static Supplier<Boolean> firstCondition;
    public static Supplier<Boolean> endCOndition;

    public LogAnalysis() {

        lastRobotState = null;

        timeInCurrentStatetimer.start();

        maxVelocity = 0;
        maxVelocityTime = 0;

        sumOfCurrents = 0;

        maxCycleTime = 0;
        minCycleTime = 100000; // just a big number
        allCycles = 0;

        cyclaeNum = 0;

        logCycleTimes = false;
        logTimeCurrentState = false;
        logMaxVelocity = false;
        logCurrent = false;
        logIsAuto = false;
        logVoltage = false;
    }


    private static double getTimeInCurrentState() {
        if (lastRobotState == null) {
            lastRobotState = currentRobotState.get();
        }

        if (currentRobotState.get() == lastRobotState) {
            return timeInCurrentStatetimer.get();
        } else {
            lastRobotState = currentRobotState.get();
            timeInCurrentStatetimer.restart();
            return 0;
        }
    }

    private static double getCycleTime() {

        boolean enterTheFirstCondition = false; // add it to make sure it enterd the first condition

        if (currentRobotState.get().getStateName() == firstRobotState.getStateName() && firstCondition.get()) {
            timebetweenStatestimer.start();
            enterTheFirstCondition = true;
        } else if (currentRobotState.get() == secondRobotState && endCOndition.get() && enterTheFirstCondition) {
            allCycles += timebetweenStatestimer.get();
            cyclaeNum ++;
            return timebetweenStatestimer.get();
        }
        return -1;
    }

    private static double getMaxCycleTime() {
        if (getCycleTime() > maxCycleTime ) {
            maxCycleTime = getCycleTime();
        }
        return maxCycleTime;
    }

    private static double getMinCycleTime() {
        if (minCycleTime < getCycleTime()) {
            minCycleTime = getCycleTime();
        }
        return minCycleTime;
    }

    private static void findingMaxVelocityAndWhoLongIsIt() {
        if (currentVelocity.get() > maxVelocity) {
            maxVelocity = currentVelocity.get();
        }
        if (Math.abs(currentVelocity.get() - maxVelocity) < LogAnalisesConstants.VELOCITY_TOLERANC) {
            timeInMaxSpeed.start();
            maxVelocityTime = timeInMaxSpeed.get();
        } else {
            timeInMaxSpeed.reset();
        }

    }

    private static double getMaxVelocity() {
        findingMaxVelocityAndWhoLongIsIt();
        return maxVelocity;
    }

    private static double getMaxVelocityTime() {
        findingMaxVelocityAndWhoLongIsIt();
        return maxVelocityTime;
    }

    private static void runCurrent() {
        for (int i = 0; i < subsystems.length - 1; i++) {
            systemCurrents[i] += subsystems[i].getCurrent();
            currentPrecents[i] = new MeasureEntry(subsystems[i].getName(), systemCurrents[i] / sumOfCurrents * 100);

            MALog.log("/Log Analyze/currents/" + subsystems[1].getName(), systemCurrents[i] / sumOfCurrents * 100);
        }
    }

    private static double getTotalCurrent() {
        sumOfCurrents += PDH.getTotalCurrent();
        return sumOfCurrents;
    }

    private static void runVoltage() {
        for (int i = 0; i < subsystems.length - 1; i++) {
            systemVoltage[i] += subsystems[i].getAppliedVolts();
            voltagePrecents[i] = new MeasureEntry(subsystems[i].getName(), systemVoltage[i] / sumOfVoltage * 100);

            MALog.log("/Log Analyze/currents/" + subsystems[1].getName(), systemCurrents[i] / sumOfVoltage * 100);
        }
    }

    private static double getTotalVoltage() {
        sumOfVoltage += PDH.getVoltage();
        return sumOfVoltage;
    }

    private static void StartAuto() {
        if (DriverStation.isAutonomousEnabled()) {//The timer will be repatedly resterded when auto is runing. should be once when auto is starting (maybe make a resetAutoTime function and you can later put it in the AutoInit of th erobot)
            autoTimer.start();
        }
    }

    public static void endAuto() { // to put in the end of the auto 👍👍👍👍👍 
        autoTime = autoTimer.get();
        autoTimer.stop();
    }

    private static double getAutoTime() {
        return autoTime;
    }

    public static void analaysCycleTimes(RobotState enterFirstRobotState, RobotState enterSecoundRobotState, Supplier<Boolean> startigCondition,
     Supplier<Boolean> endCondition, Supplier<RobotState> enterCurrentRobotState) {
        firstRobotState = enterFirstRobotState;
        secondRobotState = enterSecoundRobotState;
        currentRobotState = enterCurrentRobotState;

        firstCondition = startigCondition;
        endCOndition = endCondition;
        logCycleTimes = true;
    }

    public static void analaysTimeInCurrentState(Supplier<RobotState> enterCurrentRobotState) {
        currentRobotState = enterCurrentRobotState;
        logTimeCurrentState = true;
    }

    public static void analaysMaxVelocity(Supplier<Double> enterCurrentVelocity) {
        currentVelocity = enterCurrentVelocity;
        logMaxVelocity = true;
    }

    public static void analaysCurrents(PowerControlledSystem... newSubsystems) {
        subsystems = newSubsystems;

        systemCurrents = new double[subsystems.length];
        currentPrecents = new MeasureEntry[subsystems.length];

        logCurrent = true;
    }

    public static void analaysVoltage(PowerControlledSystem... newSubsystems) {
        subsystems = newSubsystems;

        systemVoltage = new double[subsystems.length];
        voltagePrecents = new MeasureEntry[subsystems.length];

        logVoltage = true;
    }

    public static void analaysAuto() {
        logIsAuto = true;
    }

    public static void update() {
        if (logCycleTimes) {
            MALog.log("/Log Analyze/Current Cycle Time", getCycleTime());
            MALog.log("/Log Analyze/Max Cycle time", getMaxCycleTime());
            MALog.log("/Log Analyze/Min Cycle time",getMinCycleTime());

            MALog.log("/Log Analyze/Avrege Cycle Time", allCycles / cyclaeNum);
        }

        if (logTimeCurrentState) {
            MALog.log("/Log Analyze/Current Time In State", getTimeInCurrentState());
        }

        if (logMaxVelocity) {
            MALog.log("/Log Analyze/Max Velocity", getMaxVelocity());
            MALog.log("/Log Analyze/Max Velocity Time", getMaxVelocityTime());

            //Ask rader/ari/cahtgpt about acceleration 👍👍👍
        }

        if (logCurrent) {
            getTotalCurrent();
            runCurrent();
            System.out.println(PDH.getTotalCurrent());
        }

        if (logVoltage) {
            getTotalVoltage();
            runVoltage();
            System.out.println(PDH.getVoltage());
        }

        if (logIsAuto) {
            StartAuto();
            MALog.log("/Log Analyze/Auto Time", getAutoTime()); 
        }

    }


}
