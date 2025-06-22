
package com.MAutils.RobotControl;

import com.MAutils.Logger.MALog;
import com.MAutils.RobotControl.RobotControlConstants.SystemMode;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class StateSubsystem extends SubsystemBase {

    private SubsystemState currentState;
    private SystemMode systemMode;
    public final String subsystemName;

    public StateSubsystem(String name, SubsystemState... subsystemStates) {
        for (SubsystemState state : subsystemStates) {
            if (state != null) {
                state.setSubsystem(this);
            }
        }

        currentState = new SubsystemState("IDLE", this);
        systemMode = SystemMode.AUTOMATIC;
        

        this.subsystemName = name;
    }

    public void setState(SubsystemState state) {
        currentState = state;
    }

    public SubsystemState getCurrentState() {
        return currentState;
    }

    public void setSystemMode(SystemMode mode) {
        this.systemMode = mode;
    }

    public SystemMode getSystemMode() {
        return systemMode;
    }

    public abstract Command getSelfTest();

    public abstract boolean CAN_MOVE();

    @Override
    public void periodic() {
        MALog.log("/RobotControl/" + subsystemName + "/Current State", currentState.stateName);
        MALog.log("/RobotControl/" + subsystemName + "/System Function State", getSystemMode().name());
        MALog.log("/RobotControl/" + subsystemName + "/Can Move", CAN_MOVE());
    }

}
