
package com.MAutils.RobotControl;


public class RobotState {

    private SubsystemState[] subsystemStates;
    public final String stateName;
    private Runnable onStateSet;

    public RobotState(String name , SubsystemState... subsystemStates) {
        this.subsystemStates = subsystemStates;
        stateName = name;
    }

    public RobotState(String name , Runnable onStateSet ,SubsystemState... subsystemStates) {
        this.subsystemStates = subsystemStates;
        stateName = name;
        this.onStateSet = onStateSet;

    }

    public void setState() {
        if (onStateSet != null) {
            onStateSet.run();
        }

        for (SubsystemState state : subsystemStates) {
            state.setState();
        }
    }

}
