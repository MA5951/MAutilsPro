package com.MAutils.Subsystems.DeafultSubsystems.IOs.PowerControlled;

import com.MAutils.CanBus.StatusSignalsRunner;
import com.MAutils.Components.Motor;
import com.MAutils.Logger.MALog;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PowerSystemConstants;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.PowerSystemIO;
import com.MAutils.Utils.ConvUtil;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.StrictFollower;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;

public class PowerIOReal implements PowerSystemIO {

    protected final VoltageOut voltageRequest = new VoltageOut(0);
    protected final TalonFXConfiguration motorConfig = new TalonFXConfiguration();
    private final MotorOutputConfigs brakeConfig = new MotorOutputConfigs();
    protected StrictFollower follower;

    private StatusSignal<AngularVelocity> motorVelocity;
    private StatusSignal<Angle> motorPosition;
    private StatusSignal<Current> motorCurrent;
    private StatusSignal<Voltage> motorVoltage;

    protected final String logPath;


    private final PowerSystemConstants systemConstants;

    public PowerIOReal(PowerSystemConstants systemConstants) {
        this.systemConstants = systemConstants;
        logPath = systemConstants.LOG_PATH == null ? "/Subsystems/" + systemConstants.systemName + "/IO" : systemConstants.LOG_PATH;

        configMotors();

        motorVelocity = systemConstants.master.motorController.getVelocity(false);
        motorCurrent = systemConstants.master.motorController.getStatorCurrent(false);
        motorVoltage = systemConstants.master.motorController.getMotorVoltage(false);
        motorPosition = systemConstants.master.motorController.getPosition(false);
        StatusSignalsRunner.registerSignals(systemConstants.master.canBusID, motorVelocity, motorCurrent,
                motorVoltage, motorPosition);

        motorConfig.MotorOutput.Inverted = systemConstants.master.invert;
        systemConstants.master.motorController.getConfigurator().apply(motorConfig);

        follower = new StrictFollower(systemConstants.master.canBusID.id);

        for (Motor motor : systemConstants.MOTORS) {
            motorConfig.MotorOutput.Inverted = motor.invert;
            motor.motorController.getConfigurator().apply(motorConfig);
            motor.motorController.setControl(follower);
        }

    }

    public PowerSystemConstants getSystemConstants() {
        return systemConstants;
    }

    protected void configMotors() {
        motorConfig.Feedback.SensorToMechanismRatio = systemConstants.GEAR;

        motorConfig.MotorOutput.NeutralMode = systemConstants.IS_BRAKE
                ? NeutralModeValue.Brake
                : NeutralModeValue.Coast;

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
        systemConstants.master.motorController.setControl(voltageRequest.withOutput(volt)
                .withLimitForwardMotion(Math.abs(getCurrent()) > systemConstants.MOTOR_LIMIT_CURRENT)
                .withLimitReverseMotion(Math.abs(getCurrent()) > systemConstants.MOTOR_LIMIT_CURRENT));
    }

    @Override
    public void updatePeriodic() {
        MALog.log(logPath + "/Velocity", getVelocity());
        MALog.log(logPath + "/Voltage", getAppliedVolts());
        MALog.log(logPath + "/Current", getCurrent());
        MALog.log(logPath + "/Position", getPosition());

    }

    @Override
    public boolean isMoving() {
        return ConvUtil.RPStoRPM(motorVelocity.getValueAsDouble()) > 1;
    }

    @Override
    public void restPosition(double position) {
        systemConstants.master.motorController.setPosition(position / systemConstants.POSITION_FACTOR);
    }

}
