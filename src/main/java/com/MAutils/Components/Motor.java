package com.MAutils.Components;

import com.MAutils.Utils.CANBusID;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.math.system.plant.DCMotor;

public class Motor {

        public final TalonFX motorController;
        public final DCMotor dcMotor;
        public final String name;
        public final InvertedValue invert;
        
        public Motor(CANBusID motorID, DCMotor dcMotor, String name, InvertedValue invert) {
            this.invert = invert;
            this.motorController = new TalonFX(motorID.id, motorID.bus);
            this.dcMotor = dcMotor;
            this.name = name;

        }


}