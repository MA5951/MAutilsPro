
package com.MAutils.Simulation;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Rotation;
import static edu.wpi.first.units.Units.Volts;

import java.io.ObjectInputFilter.Status;

import com.MAutils.Components.Motor.MotorType;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.DeafultSystemConstants;
import com.MAutils.Utils.SimStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.MotionMagicDutyCycle;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

@SuppressWarnings("rawtypes")
public class SimulatedTalonFX {

    private final DeafultSystemConstants constants;
    private final DCMotorSim motorSim;
    private final TalonFXConfiguration config;

    public SimulatedTalonFX(DeafultSystemConstants systemConstants, TalonFXConfiguration config,
            StatusSignal... statusSignals) {
        constants = systemConstants;
        this.config = config;

        motorSim = new DCMotorSim(
                systemConstants.systemID,
                MotorType.getDcMotor(systemConstants.master.motorType, 1 + systemConstants.MOTORS.length));

        for (StatusSignal signal : statusSignals) {
            switch (signal.getName()) {
                case "Velocity":
                    signal = getVelocitySignal();
                    break;
                case "Position":
                    signal = getPositionSignal();
                    break;
                case "ClosedLoopError":
                    signal = getErrorSignal();
                    break;
                case "ClosedLoopReference":
                    signal = getSetPointSignal();
                    break;
                case "MotorVoltage":
                    signal = getVoltageSignal();
                    break;
                default:
                    DriverStation.reportError("Unknowen Status Signal Type", false);
                    break;
            }
        }

    }

    public StatusSignal getVelocitySignal() {
        return new SimStatusSignal<AngularVelocity>(() -> motorSim.getAngularVelocity());
    }

    public StatusSignal getPositionSignal() {
        return new SimStatusSignal<Angle>(() -> Angle.ofBaseUnits(motorSim.getAngularPositionRotations(), Rotation));
    }

    public StatusSignal getErrorSignal() {
        return new SimStatusSignal<Angle>(() -> Angle.ofBaseUnits(motorSim.getAngularPositionRotations(), Rotation));
    }

    public StatusSignal getSetPointSignal() {
        return new SimStatusSignal<Angle>(() -> Angle.ofBaseUnits(motorSim.getAngularPositionRotations(), Rotation));
    }

    public StatusSignal getVoltageSignal() {
        return new SimStatusSignal<Voltage>(() -> Voltage.ofBaseUnits(motorSim.getInputVoltage(), Volts));
    }

    public StatusSignal getCurrentSignal() {
        return new SimStatusSignal<Current>(() -> Current.ofBaseUnits(motorSim.getCurrentDrawAmps(), Amps));
    }

    public void setControl(MotionMagicDutyCycle request) {
    }

    public void setControl(VelocityVoltage request) {
    }

    public void setControl(VelocityDutyCycle request) {
    }

    public void setControl(PositionVoltage request) {
    }

    public void setControl(PositionDutyCycle request) {
    }

    public void setControl(VoltageOut request) {
    }

    public void setControl(DutyCycleOut request) {
    }

    public void setControl(MotionMagicVoltage request) {
    }

    public void setVoltage(double voltage) {
    }

    public void setControl() {
    }

    public void setBrakeMode(boolean isBrake) {

    }

    public void updatePeriodic() {
    }

}
