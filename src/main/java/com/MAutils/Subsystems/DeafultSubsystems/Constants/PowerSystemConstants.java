
package com.MAutils.Subsystems.DeafultSubsystems.Constants;

import com.MAutils.Components.Motor;

public class PowerSystemConstants {
    public Motor[] MOTORS;
    public double GEAR = 1;
    public double STATOR_CURRENT_LIMIT = 40;
    public boolean CURRENT_LIMIT_ENABLED = true;
    public double MOTOR_LIMIT_CURRENT = 40;
    public String LOG_PATH = "/Subsystem/Did Not Specifiyed Log Path!";
    public boolean IS_BRAKE = true;
    public double PEAK_FORWARD_VOLTAGE = 12;
    public double PEAK_REVERSE_VOLTAGE = -12;
    public boolean FOC = false;
    public double INERTIA = 0.0004;
    public double POSITION_FACTOR = 1;
    public double VELOCITY_FACTOR = 1;

    public PowerSystemConstants() {

    }

    public PowerSystemConstants withMotors(Motor[] motors) {
        this.MOTORS = motors;
        return this;
    }

    public PowerSystemConstants withGear(double gear) {
        this.GEAR = gear;
        return this;
    }

    public PowerSystemConstants withStatorCurrentLimit(boolean currentLimitEnabled, double statorCurrentLimit) {
        this.STATOR_CURRENT_LIMIT = statorCurrentLimit;
        this.CURRENT_LIMIT_ENABLED = currentLimitEnabled;
        return this;
    }

    public PowerSystemConstants withMotorCurrentLimit(double motorLimitCurrent) {
        this.MOTOR_LIMIT_CURRENT = motorLimitCurrent;
        return this;
    }

    public PowerSystemConstants withLogPath(String logPath) {
        this.LOG_PATH = logPath;
        return this;
    }

    public PowerSystemConstants withIsBrake(boolean isBrake) {
        this.IS_BRAKE = isBrake;
        return this;
    }

    public PowerSystemConstants withPeakVoltage(double peakForwardVoltage, double peakReverseVoltage) {
        this.PEAK_FORWARD_VOLTAGE = peakForwardVoltage;
        this.PEAK_REVERSE_VOLTAGE = peakReverseVoltage;
        return this;
    }

    public PowerSystemConstants withFOC(boolean foc) {
        this.FOC = foc;
        return this;
    }

    public PowerSystemConstants withInertia(double inertia) {
        this.INERTIA = inertia;
        return this;
    }

    public PowerSystemConstants withPositionFactor(double positionFactor) {
        this.POSITION_FACTOR = positionFactor;
        return this;
    }

    public PowerSystemConstants withVelocityFactor(double velocityFactor) {
        this.VELOCITY_FACTOR = velocityFactor;
        return this;
    }

}
