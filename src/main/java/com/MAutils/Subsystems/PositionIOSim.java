package com.ma5951.utils.subsystems;

import edu.wpi.first.wpilibj.simulation.TalonFXSim;
import edu.wpi.first.wpilibj.controller.PIDController;

public class PositionIOSim extends BaseIO<PositionSystemConstants> {
    private final TalonFXSim motorSim;
    private final PIDController pid;

    public PositionIOSim(PositionSystemConstants constants) {
        super(constants);
        motorSim = new TalonFXSim(constants.motorPorts[0]);
        pid = new PIDController(constants.getP(), constants.getI(), constants.getD());
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

    public void moveTo(double setpoint) {
        double output = pid.calculate(getPosition(), setpoint) + constants.getkF() * setpoint;
        setVoltage(output);
    }
}
