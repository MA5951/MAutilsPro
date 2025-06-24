
package com.MAutils.Subsystems.DeafultSubsystems.Constants;

import com.MAutils.Components.Motor;

@SuppressWarnings("unchecked")
public class DeafultSystemConstants<T> {
    public final Motor[] MOTORS;
    public double GEAR = 1;
    public double STATOR_CURRENT_LIMIT = 40;
    public boolean CURRENT_LIMIT_ENABLED = true;
    public double MOTOR_LIMIT_CURRENT = 400;
    public String LOG_PATH = null;
    public boolean IS_BRAKE = true;
    public double PEAK_FORWARD_VOLTAGE = 12;
    public double PEAK_REVERSE_VOLTAGE = -12;
    public boolean FOC = false;
    public double INERTIA = 0.01;
    public double POSITION_FACTOR = 360;
    public double VELOCITY_FACTOR = 60;

    public DeafultSystemConstants(Motor... motors) {
        this.MOTORS = motors;
    }

    
    public T withGear(double gear) {
        this.GEAR = gear;
        return (T) this;
    }

    public T withStatorCurrentLimit(boolean currentLimitEnabled, double statorCurrentLimit) {
        this.STATOR_CURRENT_LIMIT = statorCurrentLimit;
        this.CURRENT_LIMIT_ENABLED = currentLimitEnabled;
        return (T) this;
    }

    public T withMotorCurrentLimit(double motorLimitCurrent) {
        this.MOTOR_LIMIT_CURRENT = motorLimitCurrent;
        return (T) this;
    }

    public T withLogPath(String logPath) {
        this.LOG_PATH = logPath;
        return (T) this;
    }

    public T withIsBrake(boolean isBrake) {
        this.IS_BRAKE = isBrake;
        return (T) this;
    }

    public T withPeakVoltage(double peakForwardVoltage, double peakReverseVoltage) {
        this.PEAK_FORWARD_VOLTAGE = peakForwardVoltage;
        this.PEAK_REVERSE_VOLTAGE = peakReverseVoltage;
        return (T) this;
    }

    public T withFOC(boolean foc) {
        this.FOC = foc;
        return (T) this;
    }

    public T withInertia(double inertia) {
        this.INERTIA = inertia;
        return (T) this;
    }

    public T withPositionFactor(double positionFactor) {
        this.POSITION_FACTOR = positionFactor;
        return (T) this;
    }

    public T withVelocityFactor(double velocityFactor) {
        this.VELOCITY_FACTOR = velocityFactor;
        return (T) this;
    }

}
