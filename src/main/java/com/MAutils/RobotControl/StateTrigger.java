
package com.MAutils.RobotControl;

import java.util.function.BooleanSupplier;

import com.MAutils.RobotControl.RobotControlConstants.RobotMode;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class StateTrigger {

    private RobotMode robotMode;
    private BooleanSupplier condition;
    private RobotState stateToSet;
    private RobotState stateIn;
    
    private StateTrigger(BooleanSupplier condition , RobotState stateToSet) {
        this.condition = condition;
        this.stateToSet = stateToSet;
        robotMode = null;
        stateIn = null;
    }

    public StateTrigger withRobotMode(RobotMode robotMode) {
        this.robotMode = robotMode;
        return this;
    }

    public StateTrigger withInRobotState(RobotState robotState) {
        this.stateIn = robotState;
        return this;
    }

    public Trigger build() {
        if (robotMode != null && stateIn != null) {
            return new Trigger(() -> condition.getAsBoolean() && DeafultRobotContainer.getRobotState() == stateIn && RobotControlConstants.getRobotMode() == robotMode)
                .onTrue(new InstantCommand(() -> stateToSet.setState()));
        } else if (robotMode != null) {
            return new Trigger(() -> condition.getAsBoolean() && RobotControlConstants.getRobotMode() == robotMode)
                .onTrue(new InstantCommand(() -> stateToSet.setState()));
        } else if (stateIn != null) {
            return new Trigger(() -> condition.getAsBoolean() && DeafultRobotContainer.getRobotState() == stateIn)
                .onTrue(new InstantCommand(() -> stateToSet.setState()));
        } 

        return new Trigger(condition)
                .onTrue(new InstantCommand(() -> stateToSet.setState()));
    } 

    public static StateTrigger T(BooleanSupplier condition , RobotState stateToSet) {
        return new StateTrigger(condition, stateToSet);
    }

}
