
package com.MAutils.Subsystems.DeafultSubsystems.Constants;

import com.MAutils.Components.Motor;
import com.MAutils.Components.Motor.MotorType;
import com.ctre.phoenix6.sim.TalonFXSimState;

import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N2;
import edu.wpi.first.math.system.LinearSystem;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

@SuppressWarnings("unchecked")
public abstract class DeafultSystemConstants<T> { //TOOD change to abstract class
    public Motor[] MOTORS = new Motor[]{};
    public final Motor master; 
    public final TalonFXSimState masterSimState; 
    public DCMotorSim motorSim;
    public double GEAR = 1;
    public double STATOR_CURRENT_LIMIT = 40;
    public boolean CURRENT_LIMIT_ENABLED = true;
    public double MOTOR_LIMIT_CURRENT = 400; //TODO never pass the 120 amp
    public String LOG_PATH = null; //TODO should be a impty string ""
    public boolean IS_BRAKE = true;
    public double PEAK_FORWARD_VOLTAGE = 12;
    public double PEAK_REVERSE_VOLTAGE = -12;
    public boolean FOC = false;
    public double INERTIA = 0.00001;
    public double POSITION_FACTOR = 360;
    public double VELOCITY_FACTOR = 60;
    public final String systemName;
    public LinearSystem<N2, N1, N2> systemID;

    //TODO add other constructor with gear and INERTIA

    public DeafultSystemConstants(String systemName,Motor master, Motor... motors) {
        this.MOTORS = motors;
        this.master = master;
        this.systemName = systemName;

        masterSimState = master.motorController.getSimState();

        this.systemID = LinearSystemId.createDCMotorSystem(MotorType.getDcMotor(master.motorType, 1 + MOTORS.length),
                INERTIA, GEAR); //TODO 

        motorSim = new DCMotorSim(
                systemID,
                MotorType.getDcMotor(master.motorType,
                        1 + MOTORS.length));
    }

    public T withGear(double gear) {
        this.GEAR = gear;
        this.systemID = LinearSystemId.createDCMotorSystem(MotorType.getDcMotor(master.motorType, 1 + MOTORS.length),
                INERTIA, GEAR);
        motorSim = new DCMotorSim(
                systemID,
                MotorType.getDcMotor(master.motorType,
                        1 + MOTORS.length));
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
        this.systemID = LinearSystemId.createDCMotorSystem(MotorType.getDcMotor(master.motorType, 1 + MOTORS.length),
                INERTIA, GEAR);
        motorSim = new DCMotorSim(
                systemID,
                MotorType.getDcMotor(master.motorType,
                        1 + MOTORS.length));
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

    public PowerSystemConstants toPowerSystemConstants() { //TODO this should be a abstacrt func ther every constants creat for itself
        return new PowerSystemConstants(systemName, master, MOTORS)
                .withFOC(FOC)
                .withGear(GEAR)
                .withInertia(INERTIA)
                .withIsBrake(IS_BRAKE)
                .withLogPath(LOG_PATH)
                .withMotorCurrentLimit(MOTOR_LIMIT_CURRENT)
                .withPeakVoltage(PEAK_FORWARD_VOLTAGE, PEAK_REVERSE_VOLTAGE)
                .withStatorCurrentLimit(CURRENT_LIMIT_ENABLED, STATOR_CURRENT_LIMIT)
                .withPositionFactor(POSITION_FACTOR)
                .withVelocityFactor(VELOCITY_FACTOR)
                ;
    }

}
