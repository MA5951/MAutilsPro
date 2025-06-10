
package com.MAutils.Subsystems.DeafultSubsystems.Systems;

import com.MAutils.RobotControl.StateSubsystem;
import com.MAutils.RobotControl.SubsystemState;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PositionSystemConstants;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.PositionSystemIO;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.PositionControlled.PositionIOReal;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.PositionControlled.PositionIOSim;

import frc.robot.Robot;

public class PositionControlledSystem extends StateSubsystem{

    protected final PositionSystemIO systemIO;

    public PositionControlledSystem(String name,PositionSystemConstants systemConstants, SubsystemState... subsystemStates) {
        super(name, subsystemStates);
        if (Robot.isReal()) {
            systemIO = new PositionIOReal(systemConstants);
        } else {
            systemIO = new PositionIOSim(systemConstants);
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

    public double getSetPoint() {
        return systemIO.getSetPoint();
    }

    public double getError() {
        return systemIO.getError();
    }

    public boolean atPoint() {
        return systemIO.atPoint();
    }

    public void setPosition(double position) {
        systemIO.setPosition(position);
    }

    public void setPosition(double position, double feedForward) {
        systemIO.setPosition(position, feedForward);
    }

    @Override
    public void periodic() {
        super.periodic();
        systemIO.updatePeriodic();
    }
    

}
