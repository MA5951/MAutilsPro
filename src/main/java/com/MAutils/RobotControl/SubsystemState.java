
package com.MAutils.RobotControl;


public class SubsystemState {

    public final String stateName;
    private StateSubsystem subsystem;
    
        public SubsystemState(String state_name, StateSubsystem subsystem) {
            this.stateName = state_name;
            this.subsystem = subsystem;
        }

        public SubsystemState(String state_name) {
            this.stateName = state_name;
        }
    
        public void updateSubsystem(StateSubsystem subsystem) {
            this.subsystem = subsystem;
        }

        public void setState() {
            if (subsystem != null) {
                subsystem.setState(this);
            } else {
                System.out.println("Subsystem is not set for " + stateName);
            }


        }

}
