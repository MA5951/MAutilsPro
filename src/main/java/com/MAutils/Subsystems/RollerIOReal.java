package com.ma5951.utils.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

public class RollerIOReal extends BaseIO<RollerSystemConstants> {
    private final TalonFX motor;

    public RollerIOReal(RollerSystemConstants constants) {
        super(constants);
        motor = new TalonFX(constants.motorPorts[0]);
    }

    @Override
    public void initialize() {
        super.initialize();
        TalonFXConfiguration cfg = new TalonFXConfiguration();
        // fill cfg from constants
        motor.configAllSettings(cfg);
        // Apply inversion if flagged
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
}
