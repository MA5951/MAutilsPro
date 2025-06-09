
package com.ma5951.utils.RobotControl.Utils;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.measure.AngularVelocity;

public class Motor {

    public Motors motorType;
    public TalonFX talonFX;
    public InvertedValue direction;
    public String name;
    public StatusSignal<AngularVelocity> statusSignal;

    public Motor(Motors motorType, TalonFX talonFX, InvertedValue direction, String name) {
        this.motorType = motorType;
        this.talonFX = talonFX;
        this.direction = direction;
        this.name = name;
        statusSignal = talonFX.getVelocity();
    }

    public enum Motors {
        KrakenX60(
                DCMotor.getKrakenX60(1)),
        Falcon500(
                DCMotor.getFalcon500(1)),
        NEO(
                DCMotor.getNEO(1)),
        NEO550(
                DCMotor.getNeo550(0)),
        KrakenX60FOC(
                DCMotor.getKrakenX60Foc(1)),
        Falcon500FOC(
                DCMotor.getFalcon500Foc(1)),;

        public final DCMotor motorType;

        private Motors(
                DCMotor motor) {

            this.motorType = motor;

        }

    }

}
