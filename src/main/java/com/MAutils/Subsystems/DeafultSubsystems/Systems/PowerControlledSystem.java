
package com.MAutils.Subsystems.DeafultSubsystems.Systems;

import com.MAutils.RobotControl.StateSubsystem;
import com.MAutils.Simulation.SimulationManager;
import com.MAutils.Simulation.Simulatables.SubsystemSimulation;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PowerSystemConstants;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.PowerSystemIO;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.PowerControlled.PowerIOReal;

import frc.robot.Robot;

public abstract class PowerControlledSystem extends StateSubsystem{

    protected final PowerSystemIO systemIO;

    public PowerControlledSystem(PowerSystemConstants systemConstants) {
        super(systemConstants.systemName);
        systemIO = new PowerIOReal(systemConstants);


        if (!Robot.isReal()) {
            SimulationManager.registerSimulatable(new SubsystemSimulation(systemIO.getSystemConstants()));
        }
    }

    public PowerControlledSystem(PowerSystemConstants systemConstants, PowerSystemIO simIO) {
        super(systemConstants.systemName);
        systemIO = new PowerIOReal(systemConstants);

        if (!Robot.isReal()) {
            SimulationManager.registerSimulatable(new SubsystemSimulation(systemIO.getSystemConstants()));
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
