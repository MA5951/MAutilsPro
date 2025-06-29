
package com.MAutils.RobotControl;

public class State {

    public final String stateName;
    private StateSubsystem subsystem;
    private Runnable onStateSet = () -> {
    };

    public State(String state_name, Runnable onStateSet, StateSubsystem subsystem) {
        this.stateName = state_name;
        this.subsystem = subsystem;
        this.onStateSet = onStateSet;
    }

    public State(String state_name, StateSubsystem subsystem) {
        this.stateName = state_name;
        this.subsystem = subsystem;
    }

    public StateSubsystem getSubsystem() {
        return subsystem;
    }

    public void runRunnable() {
        onStateSet.run();
    }

}
