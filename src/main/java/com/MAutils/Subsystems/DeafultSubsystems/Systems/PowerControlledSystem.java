
package com.MAutils.Subsystems.DeafultSubsystems.Systems;

import com.MAutils.RobotControl.StateSubsystem;
import com.MAutils.RobotControl.SubsystemState;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PowerSystemConstants;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.PowerSystemIO;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.PowerControlled.PowerIOReal;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.PowerControlled.PowerIOSim;

import frc.robot.Robot;

public class PowerControlledSystem extends StateSubsystem{

    protected final PowerSystemIO systemIO;

    public PowerControlledSystem(String name,PowerSystemConstants systemConstants, SubsystemState... subsystemStates) {
        super(name, subsystemStates);
        if (Robot.isReal()) {
            systemIO = new PowerIOReal(systemConstants);
        } else {
            systemIO = new PowerIOSim(systemConstants);
        }
    }

    public double getAppliedVolts() {
        return systemIO.getAppliedVolts();
    }

    public double getCurrent() {
        return systemIO.getCurrent();
    }

    public double getPosition() {
        return systemIO.getPosition();
    }

    public double getVelocity() {
        return systemIO.getVelocity();
    }

    public void setVoltage(double voltage) {
        systemIO.setVoltage(voltage);
    }

    public void setBrakeMode(boolean isBrake) {
        systemIO.setBrakeMode(isBrake);
    }

    @Override
    public void periodic() {
        super.periodic();
        systemIO.updatePeriodic();
    }
    

}
