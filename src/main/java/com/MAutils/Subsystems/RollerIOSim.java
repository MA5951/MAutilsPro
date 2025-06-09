package com.ma5951.utils.subsystems;

import edu.wpi.first.wpilibj.simulation.TalonFXSim;

public class RollerIOSim extends BaseIO<RollerSystemConstants> {
    private final TalonFXSim motorSim;

    public RollerIOSim(RollerSystemConstants constants) {
        super(constants);
        motorSim = new TalonFXSim(constants.motorPorts[0]);
    }

    @Override
    public double getPosition() {
        return motorSim.getPosition();
    }

    @Override
    public double getVelocity() {
        return motorSim.getVelocity();
    }

    @Override
    public void setVoltage(double volts) {
        motorSim.setBusVoltage(volts);
    }
}
