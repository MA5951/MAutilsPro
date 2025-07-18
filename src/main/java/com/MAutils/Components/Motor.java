package com.MAutils.Components;

import com.MAutils.CanBus.CANBusID;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.math.system.plant.DCMotor;

public class Motor {

    public final TalonFX motorController; //TODO chage to private and add get()
    public final MotorType motorType; //TODO chage to private and add get()
    public final String name; //TODO chage to private and add get()
    public final InvertedValue invert; //TODO chage to private and add get()
    public final CANBusID canBusID; //TODO chage to private and add get()

    public Motor(CANBusID motorID, MotorType motorType, String name, InvertedValue invert) {
            this.canBusID = motorID;
            this.invert = invert;
            this.motorController = new TalonFX(motorID.id, motorID.bus); //TODO add a doc that say we only use talonFX
            this.motorType = motorType;
            this.name = name; //TODO why we need the name?


        }

    public enum MotorType {
        KRAKEN,
        KRAKEN_FOC,
        FALCON,
        FALCON_FOC,
        NEO,
        BAG,
        CIM,
        MINI_NEO,
        MINI_CIM,
        RS775;

        public static DCMotor getDcMotor(MotorType type, int numMotors) { //TODO chage to be the motor class func
            switch (type) {
                case KRAKEN:
                    return DCMotor.getKrakenX60(numMotors);
                case KRAKEN_FOC:
                    return DCMotor.getKrakenX60(numMotors); //TODO change to foc
                case FALCON:
                    return DCMotor.getFalcon500(numMotors);
                case FALCON_FOC:
                    return DCMotor.getFalcon500Foc(numMotors);
                case NEO:
                    return DCMotor.getNEO(numMotors);
                case BAG:
                    return DCMotor.getBag(numMotors);
                case CIM:
                    return DCMotor.getCIM(numMotors);
                case MINI_NEO:
                    return DCMotor.getNeo550(numMotors);
                case MINI_CIM:
                    return DCMotor.getMiniCIM(numMotors);
                case RS775:
                    return DCMotor.getVex775Pro(numMotors);
                default:
                    throw new IllegalArgumentException("Unknown motor type: " + type);
            }
        }
    }

}