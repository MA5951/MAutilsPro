
package com.MAutils.Subsystems.DeafultSubsystems.Systems;

import com.MAutils.RobotControl.StateSubsystem;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PowerSystemConstants;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.PowerSystemIO;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.PowerControlled.PowerIOReal;

import frc.robot.Robot;

public abstract class PowerControlledSystem extends StateSubsystem{

    protected PowerSystemIO systemIO;//FINAL

    public PowerControlledSystem(String name,PowerSystemConstants systemConstants) {
        super(name);
        if (Robot.isReal()) {
            systemIO = new PowerIOReal(name, systemConstants);
        } else {
            //systemIO = new PowerIOSim(name, systemConstants);
        }
    }

    public PowerControlledSystem(String name,PowerSystemConstants systemConstants, PowerSystemIO simIO) {
        super(name);
        if (Robot.isReal()) {
            systemIO = new PowerIOReal(name, systemConstants);
        } else {
            systemIO = simIO;
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
