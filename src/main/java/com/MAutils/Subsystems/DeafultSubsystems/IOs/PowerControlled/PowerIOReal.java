package com.MAutils.Subsystems.DeafultSubsystems.IOs.PowerControlled;

import com.MAutils.Components.Motor;
import com.MAutils.Logger.MALog;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PowerSystemConstants;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.PowerSystemIO;
import com.MAutils.Utils.StatusSignalsRunner;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.StrictFollower;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;

public class PowerIOReal implements PowerSystemIO {

    private final int numOfMotors;
    private final VoltageOut voltageRequest = new VoltageOut(0);
    protected final TalonFXConfiguration motorConfig = new TalonFXConfiguration();
    private MotorOutputConfigs brakeConfig = new MotorOutputConfigs();
    private final StrictFollower[] followers;

    private StatusSignal<AngularVelocity> motorVelocity;
    private StatusSignal<Angle> motorPosition;
    private StatusSignal<Current> motorCurrent;
    private StatusSignal<Voltage> motorVoltage;

    private final PowerSystemConstants systemConstants;

    private int i = 0;

    public PowerIOReal(PowerSystemConstants systemConstants) {
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
                StatusSignalsRunner.registerSignals(motorVelocity, motorCurrent,
                        motorVoltage, motorPosition);
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
        systemConstants.MOTORS[0].motorController.setControl(voltageRequest.withOutput(volt)
                .withLimitForwardMotion(getCurrent() > systemConstants.MOTOR_LIMIT_CURRENT)
                .withLimitReverseMotion(getCurrent() > systemConstants.MOTOR_LIMIT_CURRENT));
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

        

    }



}
