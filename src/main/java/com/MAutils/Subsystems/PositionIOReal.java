package com.MAutils.Subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.controller.PIDController;

public class PositionIOReal extends BaseIO<PositionSystemConstants> {
    private final TalonFX motor;
    private final PIDController pid;

    public PositionIOReal(PositionSystemConstants constants) {
        super(constants);
        motor = new TalonFX(constants.motorPorts[0]);
        pid = new PIDController(constants.getP(), constants.getI(), constants.getD());
    }

    @Override
    public void initialize() {
        super.initialize();
        // additional real setup
        motor.setInverted(constants.motorInverted[0]);
    }

    @Override
    public double getPosition() {
        return motor.getSelectedSensorPosition();
    }

    @Override
    public double getVelocity() {
        return motor.getSelectedSensorVelocity();
    }

    @Override
    public void setVoltage(double volts) {
        motor.setVoltage(volts);
    }

    /** 
     * Move to target position using internal PID.
     */
    public void moveTo(double setpoint) {
        double output = pid.calculate(getPosition(), setpoint) + constants.getkF() * setpoint;
        setVoltage(output);
    }
}
