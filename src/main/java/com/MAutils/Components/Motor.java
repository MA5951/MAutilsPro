package com.MAutils.Components;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.math.system.plant.DCMotor;

public class Motor {

        public final TalonFX motorController;
        public final DCMotor dcMotor;
        public final String name;
        public final InvertedValue invert;
        
        public Motor(TalonFX motorController, DCMotor dcMotor, String name, InvertedValue invert) {
            this.invert = invert;
            this.motorController = motorController;
            this.dcMotor = dcMotor;
            this.name = name;

        }


}