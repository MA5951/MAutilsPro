package com.MAutils.Subsystems.DeafultSubsystems.IOs.PositionControlled;

import com.MAutils.Components.Motor;
import com.MAutils.Logger.MALog;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PositionSystemConstants;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.PositionSystemIO;
import com.MAutils.Utils.StatusSignalsRunner;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.StrictFollower;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;

public class PositionIOReal implements PositionSystemIO {

    private final int numOfMotors;
    private final VoltageOut voltageRequest = new VoltageOut(0);
    private final PositionVoltage positionRequest = new PositionVoltage(0);
    private final MotionMagicVoltage motionMagicRequest = new MotionMagicVoltage(0);
    protected final TalonFXConfiguration motorConfig = new TalonFXConfiguration();
    private MotorOutputConfigs brakeConfig = new MotorOutputConfigs();
    private final StrictFollower[] followers;

    private StatusSignal<AngularVelocity> motorVelocity;
    private StatusSignal<Angle> motorPosition;
    private StatusSignal<Double> motorError;
    private StatusSignal<Double> motorSetPoint;
    private StatusSignal<Current> motorCurrent;
    private StatusSignal<Voltage> motorVoltage;

    private final PositionSystemConstants systemConstants;

    private int i = 0;

    public PositionIOReal(PositionSystemConstants systemConstants) {
        this.systemConstants = systemConstants;
        numOfMotors = systemConstants.MOTORS.length;

        configMotors();

        followers = new StrictFollower[numOfMotors - 1];

        for (Motor motor : systemConstants.MOTORS) {
            if (i > 0) {
                followers[i - 1] = new StrictFollower(systemConstants.MOTORS[0].motorController.getDeviceID());
            } else {
                motorVelocity = motor.motorController.getVelocity(false);
                motorCurrent = motor.motorController.getStatorCurrent(false);
                motorVoltage = motor.motorController.getMotorVoltage(false);
                motorPosition = motor.motorController.getPosition(false);
                motorError = motor.motorController.getClosedLoopError(false);
                motorSetPoint = motor.motorController.getClosedLoopReference(false);
                StatusSignalsRunner.registerSignals(motorVelocity, motorCurrent,
                        motorVoltage, motorError, motorSetPoint, motorPosition);
            }
            TalonFX.resetSignalFrequenciesForAll(motor.motorController);
            motorConfig.MotorOutput.Inverted = motor.invert;
            motor.motorController.getConfigurator().apply(motorConfig);
            i++;
        }

    }

    protected void configMotors() {
        motorConfig.Feedback.SensorToMechanismRatio = systemConstants.GEAR;

        motorConfig.MotorOutput.NeutralMode = systemConstants.IS_BRAKE
                ? NeutralModeValue.Brake
                : NeutralModeValue.Coast;

        motorConfig.Slot0.kP = systemConstants.P_GAIN;
        motorConfig.Slot0.kI = systemConstants.I_GAIN;
        motorConfig.Slot0.kD = systemConstants.D_GAIN;

        motorConfig.MotionMagic.MotionMagicAcceleration = systemConstants.ACCELERATION;
        motorConfig.MotionMagic.MotionMagicCruiseVelocity = systemConstants.CRUISE_VELOCITY;
        motorConfig.MotionMagic.MotionMagicJerk = systemConstants.JERK;

        motorConfig.Voltage.PeakForwardVoltage = systemConstants.PEAK_FORWARD_VOLTAGE;
        motorConfig.Voltage.PeakReverseVoltage = systemConstants.PEAK_REVERSE_VOLTAGE;

        motorConfig.CurrentLimits.StatorCurrentLimit = systemConstants.STATOR_CURRENT_LIMIT;
        motorConfig.CurrentLimits.StatorCurrentLimitEnable = systemConstants.CURRENT_LIMIT_ENABLED;
    }

    @Override
    public double getCurrent() {
        return motorCurrent.getValueAsDouble();
    }

    @Override
    public double getAppliedVolts() {
        return motorVoltage.getValueAsDouble();
    }

    @Override
    public double getVelocity() {
        return motorVelocity.getValueAsDouble() * systemConstants.VELOCITY_FACTOR;
    }

    @Override
    public double getPosition() {
        return motorPosition.getValueAsDouble() * systemConstants.POSITION_FACTOR;
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
    public void setBrakeMode(boolean isBrake) {
        brakeConfig.NeutralMode = isBrake
                ? NeutralModeValue.Brake
                : NeutralModeValue.Coast;

        for (Motor motor : systemConstants.MOTORS) {
            motorConfig.MotorOutput.Inverted = motor.invert;
            motor.motorController.getConfigurator().apply(brakeConfig);
        }
    }

    @Override
    public void setVoltage(double volt) {
        systemConstants.MOTORS[0].motorController.setControl(voltageRequest.withOutput(volt)
        .withLimitForwardMotion(getCurrent() > systemConstants.MOTOR_LIMIT_CURRENT || getPosition() > systemConstants.MAX_POSE)
        .withLimitReverseMotion(getCurrent() < systemConstants.MOTOR_LIMIT_CURRENT || getPosition() < systemConstants.MIN_POSE));
        i = 1;
        while (i < numOfMotors) {
            systemConstants.MOTORS[i].motorController.setControl(followers[i - 1]);
        }
    }

    @Override
    public void setPosition(double position) {
        if (systemConstants.IS_MOTION_MAGIC) {
            systemConstants.MOTORS[0].motorController.setControl(
                    motionMagicRequest.withPosition(position / systemConstants.POSITION_FACTOR)
                            .withSlot(0)
                            .withFeedForward(systemConstants.F_GAIN)
                            .withLimitForwardMotion(getCurrent() > systemConstants.MOTOR_LIMIT_CURRENT || getPosition() > systemConstants.MAX_POSE)
                            .withLimitReverseMotion(getCurrent() < systemConstants.MOTOR_LIMIT_CURRENT || getPosition() < systemConstants.MIN_POSE));
        } else {
            systemConstants.MOTORS[0].motorController.setControl(
                    positionRequest.withPosition(position / systemConstants.POSITION_FACTOR)
                            .withSlot(0)
                            .withFeedForward(systemConstants.F_GAIN)
                            .withLimitForwardMotion(getCurrent() > systemConstants.MOTOR_LIMIT_CURRENT || getPosition() > systemConstants.MAX_POSE)
                            .withLimitReverseMotion(getCurrent() < systemConstants.MOTOR_LIMIT_CURRENT || getPosition() < systemConstants.MIN_POSE));
        }
        i = 1;
        while (i < numOfMotors) {
            systemConstants.MOTORS[i].motorController.setControl(followers[i - 1]);
        }
    }

    @Override
    public void setPosition(double position, double voltageFeedForward) {
        if (systemConstants.IS_MOTION_MAGIC) {
            systemConstants.MOTORS[0].motorController.setControl(
                    motionMagicRequest.withPosition(position / systemConstants.POSITION_FACTOR)
                            .withSlot(0)
                            .withFeedForward(voltageFeedForward)
                            .withLimitForwardMotion(getCurrent() > systemConstants.MOTOR_LIMIT_CURRENT || getPosition() > systemConstants.MAX_POSE)
                            .withLimitReverseMotion(getCurrent() < systemConstants.MOTOR_LIMIT_CURRENT || getPosition() < systemConstants.MIN_POSE));
        } else {
            systemConstants.MOTORS[0].motorController.setControl(
                    positionRequest.withPosition(position / systemConstants.POSITION_FACTOR)
                            .withSlot(0)
                            .withFeedForward(voltageFeedForward)
                            .withLimitForwardMotion(getCurrent() > systemConstants.MOTOR_LIMIT_CURRENT || getPosition() > systemConstants.MAX_POSE)
                            .withLimitReverseMotion(getCurrent() < systemConstants.MOTOR_LIMIT_CURRENT || getPosition() < systemConstants.MIN_POSE));
        }
        i = 1;
        while (i < numOfMotors) {
            systemConstants.MOTORS[i].motorController.setControl(followers[i - 1]);
        }
    }

    @Override
    public void updatePeriodic() {
        MALog.log("/Subsystem/" + systemConstants.LOG_PATH + "/IO/" + "/Velocity", getVelocity());
        MALog.log("/Subsystem/" + systemConstants.LOG_PATH + "/IO/" + "/Voltage", getAppliedVolts());
        MALog.log("/Subsystem/" + systemConstants.LOG_PATH + "/IO/" + "/Current", getCurrent());
        MALog.log("/Subsystem/" + systemConstants.LOG_PATH + "/IO/" + "/Position", getPosition());
        MALog.log("/Subsystem/" + systemConstants.LOG_PATH + "/IO/" + "/Set Point", getSetPoint());
        MALog.log("/Subsystem/" + systemConstants.LOG_PATH + "/IO/" + "/Error", getError());
        MALog.log("/Subsystem/" + systemConstants.LOG_PATH + "/IO/" + "/At Point", atPoint());

    }

}
