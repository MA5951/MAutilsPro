
package com.MAutils.Subsystems.DeafultSubsystems.Systems;

import com.MAutils.CanBus.StatusSignalsRunner;
import com.MAutils.RobotControl.StateSubsystem;
import com.MAutils.Simulation.SimulationManager;
import com.MAutils.Simulation.Simulatables.SubsystemSimulation;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PositionSystemConstants;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.PositionSystemIO;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.PositionControlled.PositionIOReal;
import com.ctre.phoenix6.StatusSignal;

import frc.robot.Robot;

public abstract class PositionControlledSystem extends StateSubsystem {

    protected final PositionSystemIO systemIO;

    public PositionControlledSystem(PositionSystemConstants systemConstants,
            @SuppressWarnings("rawtypes") StatusSignal... statusSignals) {
        super(systemConstants.systemName);
        
        systemIO = new PositionIOReal(systemConstants);

        if (!Robot.isReal()) {
            SimulationManager.registerSimulatable(new SubsystemSimulation(systemIO.getSystemConstants()));
        }

        StatusSignalsRunner.registerSignals(systemConstants.master.canBusID, statusSignals);
    }

    public PositionControlledSystem(PositionSystemConstants systemConstants, PositionSystemIO simIO) {
        super(systemConstants.systemName);
        systemIO = new PositionIOReal(systemConstants); //TODO why you pass sim IO?

        if (!Robot.isReal()) { 
            SimulationManager.registerSimulatable(new SubsystemSimulation(systemIO.getSystemConstants()));
        }
        //TODO why you dont pass statusSignals
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
