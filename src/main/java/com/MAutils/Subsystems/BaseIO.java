package com.MAutils.Subsystems;

import edu.wpi.first.util.logging.Logger;

/**
 * Generic hardware interface for a subsystem.
 */
public abstract class BaseIO<C extends BaseSystemConstants> {
    protected final C constants;
    protected final Logger logger = Logger.getLogger(getClass().getName());

    public BaseIO(C constants) {
        this.constants = constants;
    }

    /**
     * Configure motors and apply default settings.
     */
    public void initialize() {
        configMotors();
        applyDefaultSettings();
    }

    protected void configMotors() {
        for (int i = 0; i < constants.motorPorts.length; i++) {
            // Example: TalonFX motor = new TalonFX(constants.motorPorts[i]);
            // motor.configFactoryDefault();
            // Apply inversion if provided
            // motor.setInverted(constants.motorInverted[i]);
        }
    }

    protected void applyDefaultSettings() {
        // set current limits, sensor phases, etc.
    }

    protected void logSensors() {
        // logger.log(constants.logPath + "/position", getPosition());
        // logger.log(constants.logPath + "/velocity", getVelocity());
    }

    public void periodic() {
        logSensors();
    }

    public abstract double getPosition();
    public abstract double getVelocity();
    public abstract void setVoltage(double volts);
}
