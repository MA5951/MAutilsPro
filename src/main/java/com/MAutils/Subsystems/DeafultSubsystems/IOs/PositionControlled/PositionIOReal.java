package com.MAutils.Subsystems.DeafultSubsystems.IOs.PositionControlled;

import com.MAutils.CanBus.StatusSignalsRunner;
import com.MAutils.Components.Motor;
import com.MAutils.Logger.MALog;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PositionSystemConstants;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PowerSystemConstants;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.PositionSystemIO;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.PowerControlled.PowerIOReal;
import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.StrictFollower;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;

public class PositionIOReal extends PowerIOReal implements PositionSystemIO {

    private PositionSystemConstants systemConstants;

    private StatusSignal<Double> motorError;
    private StatusSignal<Double> motorSetPoint;

    public PositionIOReal(String subsystemName, PositionSystemConstants systemConstants) {
        super(subsystemName, systemConstants.toPowerSystemConstants());
        this.systemConstants = systemConstants;

        motorError = systemConstants.master.motorController.getClosedLoopError(false);
        motorSetPoint = systemConstants.master.motorController.getClosedLoopReference(false);
    }

    @Override
    protected void configMotors() {//TODO cheack inthritance
        super.configMotors();
        motorConfig.Slot0.kP = systemConstants.getGainConfig().Kp;
        motorConfig.Slot0.kI = systemConstants.getGainConfig().Ki;
        motorConfig.Slot0.kD = systemConstants.getGainConfig().Kd;
        motorConfig.Slot0.kS = systemConstants.getGainConfig().Ks;
        motorConfig.Slot0.StaticFeedforwardSign = StaticFeedforwardSignValue.UseClosedLoopSign;

        motorConfig.MotionMagic.MotionMagicAcceleration = systemConstants.ACCELERATION;
        motorConfig.MotionMagic.MotionMagicCruiseVelocity = systemConstants.CRUISE_VELOCITY;
        motorConfig.MotionMagic.MotionMagicJerk = systemConstants.JERK;
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
        systemConstants.MOTORS[0].motorController.setControl(voltageRequest.withOutput(volt)
                .withLimitForwardMotion(
                        Math.abs(getCurrent()) > systemConstants.MOTOR_LIMIT_CURRENT
                                || getPosition() > systemConstants.MAX_POSE)
                .withLimitReverseMotion(Math.abs(getCurrent()) < systemConstants.MOTOR_LIMIT_CURRENT
                        || getPosition() < systemConstants.MIN_POSE));
        for (Motor motor : systemConstants.MOTORS) {
            motor.motorController.setControl(follower);
        }
    }

    @Override
    public void setPosition(double position) {

        if (systemConstants.IS_MOTION_MAGIC) {
            systemConstants.MOTORS[0].motorController.setControl(
                    motionMagicRequest.withPosition(position / systemConstants.POSITION_FACTOR)
                            .withSlot(0)
                            .withFeedForward(systemConstants.getGainConfig().Kf)
                            .withLimitForwardMotion(Math.abs(getCurrent()) > systemConstants.MOTOR_LIMIT_CURRENT
                                    || getPosition() > systemConstants.MAX_POSE)
                            .withLimitReverseMotion(Math.abs(getCurrent()) < systemConstants.MOTOR_LIMIT_CURRENT
                                    || getPosition() < systemConstants.MIN_POSE));
        } else {
            systemConstants.MOTORS[0].motorController.setControl(
                    positionRequest.withPosition(position / systemConstants.POSITION_FACTOR)
                            .withSlot(0)
                            .withFeedForward(systemConstants.getGainConfig().Kf)
                            .withLimitForwardMotion(Math.abs(getCurrent()) > systemConstants.MOTOR_LIMIT_CURRENT
                                    || getPosition() > systemConstants.MAX_POSE)
                            .withLimitReverseMotion(Math.abs(getCurrent()) < systemConstants.MOTOR_LIMIT_CURRENT
                                    || getPosition() < systemConstants.MIN_POSE));
        }
        for (Motor motor : systemConstants.MOTORS) {
            motor.motorController.setControl(follower);
        }
    }

    @Override
    public void setPosition(double position, double voltageFeedForward) {
        if (systemConstants.IS_MOTION_MAGIC) {
            systemConstants.MOTORS[0].motorController.setControl(
                    motionMagicRequest.withPosition(position / systemConstants.POSITION_FACTOR)
                            .withSlot(0)
                            .withFeedForward(voltageFeedForward)
                            .withLimitForwardMotion(Math.abs(getCurrent()) > systemConstants.MOTOR_LIMIT_CURRENT
                                    || getPosition() > systemConstants.MAX_POSE)
                            .withLimitReverseMotion(Math.abs(getCurrent()) < systemConstants.MOTOR_LIMIT_CURRENT
                                    || getPosition() < systemConstants.MIN_POSE));
        } else {
            systemConstants.MOTORS[0].motorController.setControl(
                    positionRequest.withPosition(position / systemConstants.POSITION_FACTOR)
                            .withSlot(0)
                            .withFeedForward(voltageFeedForward)
                            .withLimitForwardMotion(Math.abs(getCurrent()) > systemConstants.MOTOR_LIMIT_CURRENT
                                    || getPosition() > systemConstants.MAX_POSE)
                            .withLimitReverseMotion(Math.abs(getCurrent()) < systemConstants.MOTOR_LIMIT_CURRENT
                                    || getPosition() < systemConstants.MIN_POSE));
        }
        for (Motor motor : systemConstants.MOTORS) {
            motor.motorController.setControl(follower);
        }
    }

    @Override
    public void updatePeriodic() {
        MALog.log(logPath + "/Set Point", getSetPoint());
        MALog.log(logPath + "/Error", getError());
        MALog.log(logPath + "/At Point", atPoint());

    }

    public void restPosition(double position) {
        systemConstants.master.motorController.setPosition(position / systemConstants.POSITION_FACTOR);
    }

    @Override
    public void setPID(double kP, double kI, double kD) {
        motorConfig.Slot0.kP = systemConstants.getGainConfig().Kp;
        motorConfig.Slot0.kI = systemConstants.getGainConfig().Ki;
        motorConfig.Slot0.kD = systemConstants.getGainConfig().Kd;

        motorConfig.MotorOutput.Inverted = systemConstants.master.invert;
        systemConstants.master.motorController.getConfigurator().apply(motorConfig);
    }

    @Override
    public boolean isMoving() {
        return getVelocity() > 1;
    }

}
