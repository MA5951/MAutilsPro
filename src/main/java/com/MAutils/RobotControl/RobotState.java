
package com.MAutils.RobotControl;

public class RobotState {

    private static StateSubsystem[] subsystemsArry;
    private State[] subsystemStates;
    private final String stateName;
    private Runnable onStateSet = () -> {

    };

    public static void registerSubsystes(StateSubsystem... subsystems) {
        subsystemsArry = subsystems;
    }

    public RobotState(String name, State... subsystemStates) {
        this.subsystemStates = subsystemStates;
        stateName = name;
    }

    public RobotState(String name, Runnable onStateSet, State... subsystemStates) {
        this.subsystemStates = subsystemStates;
        stateName = name;
        this.onStateSet = onStateSet;

    }

    public String getStateName() {
        return stateName;
    }

    public void setState() {
        onStateSet.run();

        for (StateSubsystem subsystem : subsystemsArry) {
            for (State state : subsystemStates) {
                if (state.getSubsystem().getName() == subsystem.getName()) {
                    subsystem.setState(state);
                }
            }
        }
    }

}
