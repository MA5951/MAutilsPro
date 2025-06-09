package com.MAutils.Subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Generic subsystem wiring constants + IO.
 */
public class DefaultSubsystem<C extends BaseSystemConstants, IO extends BaseIO<C>> extends SubsystemBase {
    protected final C constants;
    protected final IO io;

    public DefaultSubsystem(C constants, IO io) {
        this.constants = constants;
        this.io = io;
        io.initialize();
    }

    @Override
    public void periodic() {
        io.periodic();
    }

    public void setVoltage(double volts) {
        io.setVoltage(volts);
    }

    public double getPosition() { return io.getPosition(); }
    public double getVelocity() { return io.getVelocity(); }
}
