package com.MAutils.Subsystems.DeafultSubsystems.IOs.PositionControlled;

import com.MAutils.CanBus.StatusSignalsRunner;
import com.MAutils.Components.Motor;
import com.MAutils.Logger.MALog;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PositionSystemConstants;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.PositionSystemIO;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.PowerControlled.PowerIOReal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

public class PositionIOReal extends PowerIOReal implements PositionSystemIO {

    public final PositionSystemConstants systemConstants;

    private MotionMagicVoltage motionMagicRequest = new MotionMagicVoltage(0);
    private PositionVoltage positionRequest = new PositionVoltage(0);

    private StatusSignal<Double> motorError;
    private StatusSignal<Double> motorSetPoint;

    public PositionIOReal(PositionSystemConstants systemConstants) {
        super(systemConstants.toPowerSystemConstants());
        this.systemConstants = systemConstants;

        motorError = systemConstants.master.motorController.getClosedLoopError(false);
        motorSetPoint = systemConstants.master.motorController.getClosedLoopReference(false);
        StatusSignalsRunner.registerSignals(systemConstants.master.canBusID, motorSetPoint, motorError);

        configMotors();
    }

    @Override
    protected void configMotors() {
        motorConfig.Slot0.kP = systemConstants.getGainConfig().Kp;
        motorConfig.Slot0.kI = systemConstants.getGainConfig().Ki;
        motorConfig.Slot0.kD = systemConstants.getGainConfig().Kd;
        motorConfig.Slot0.kS = systemConstants.getGainConfig().Ks;
        motorConfig.Slot0.kV = systemConstants.getGainConfig().Kv;
        motorConfig.Slot0.kA = systemConstants.getGainConfig().Ka;
        motorConfig.Slot0.StaticFeedforwardSign = systemConstants.IS_MOTION_MAGIC
                ? StaticFeedforwardSignValue.UseVelocitySign
                : StaticFeedforwardSignValue.UseClosedLoopSign;

        motorConfig.MotionMagic.MotionMagicAcceleration = systemConstants.ACCELERATION;
        motorConfig.MotionMagic.MotionMagicCruiseVelocity = systemConstants.CRUISE_VELOCITY;
        motorConfig.MotionMagic.MotionMagicJerk = systemConstants.JERK;

        motorConfig.OpenLoopRamps.VoltageOpenLoopRampPeriod = systemConstants.RAMP_RATE;
        motorConfig.ClosedLoopRamps.VoltageClosedLoopRampPeriod = systemConstants.RAMP_RATE;

        motorConfig.MotorOutput.Inverted = systemConstants.master.invert;
        systemConstants.master.motorController.getConfigurator().apply(motorConfig);

        for (Motor motor : systemConstants.MOTORS) {
            motorConfig.MotorOutput.Inverted = motor.invert;
            motor.motorController.getConfigurator().apply(motorConfig);
            motor.motorController.setControl(follower);
        }
    }

    @Override
    public double getError() {
        return motorError.getValueAsDouble() * systemConstants.POSITION_FACTOR;
    }

    @Override
    public double getSetPoint() {
        return motorSetPoint.getValueAsDouble() * systemConstants.POSITION_FACTOR;
    }

    @Override
    public boolean atPoint() {
        return Math.abs(getError()) < systemConstants.TOLERANCE;
    }

    @Override
    public void setVoltage(double volt) {
        systemConstants.master.motorController.setControl(voltageRequest.withOutput(volt)
                .withLimitForwardMotion(
                        Math.abs(getCurrent()) > systemConstants.MOTOR_LIMIT_CURRENT
                                || getPosition() > systemConstants.MAX_POSE)
                .withLimitReverseMotion(Math.abs(getCurrent()) < systemConstants.MOTOR_LIMIT_CURRENT
                        || getPosition() < systemConstants.MIN_POSE));
    }

    @Override
    public void setPosition(double position, double voltageFeedForward) {
        if (systemConstants.IS_MOTION_MAGIC) {
            systemConstants.master.motorController.setControl(
                    motionMagicRequest.withPosition(position / systemConstants.POSITION_FACTOR)
                            .withSlot(0)
                            .withFeedForward(voltageFeedForward)
                            .withLimitForwardMotion(Math.abs(getCurrent()) > systemConstants.MOTOR_LIMIT_CURRENT
                                    || getPosition() > systemConstants.MAX_POSE)
                            .withLimitReverseMotion(Math.abs(getCurrent()) < systemConstants.MOTOR_LIMIT_CURRENT
                                    || getPosition() < systemConstants.MIN_POSE));
        } else {
            systemConstants.master.motorController.setControl(
                    positionRequest.withPosition(position / systemConstants.POSITION_FACTOR)
                            .withSlot(0)
                            .withFeedForward(voltageFeedForward)
                            .withLimitForwardMotion(Math.abs(getCurrent()) > systemConstants.MOTOR_LIMIT_CURRENT
                                    || getPosition() > systemConstants.MAX_POSE)
                            .withLimitReverseMotion(Math.abs(getCurrent()) < systemConstants.MOTOR_LIMIT_CURRENT
                                    || getPosition() < systemConstants.MIN_POSE));
        }
    }

    @Override
    public void setPosition(double position) {
        setPosition(position, systemConstants.getGainConfig().Kf);
    }

    @Override
    public void updatePeriodic() {
        super.updatePeriodic();

        MALog.log(logPath + "/Set Point", getSetPoint());
        MALog.log(logPath + "/Error", getError());
        MALog.log(logPath + "/At Point", atPoint());
        MALog.log(logPath + "/Forward Limit", Math.abs(getCurrent()) > systemConstants.MOTOR_LIMIT_CURRENT
                || getPosition() > systemConstants.MAX_POSE);
        MALog.log(logPath + "/Reverse Limit", Math.abs(getCurrent()) < systemConstants.MOTOR_LIMIT_CURRENT
                || getPosition() < systemConstants.MIN_POSE);
    }

    @Override
    public void setPID(double kP, double kI, double kD) {
        motorConfig.Slot0.kP = systemConstants.getGainConfig().Kp;
        motorConfig.Slot0.kI = systemConstants.getGainConfig().Ki;
        motorConfig.Slot0.kD = systemConstants.getGainConfig().Kd;

        motorConfig.MotorOutput.Inverted = systemConstants.master.invert;
        systemConstants.master.motorController.getConfigurator().apply(motorConfig);
    }

}
