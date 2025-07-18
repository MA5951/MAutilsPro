
package com.MAutils.Components;

import java.util.function.Supplier;

import frc.robot.Robot;

public class SensorWrapper<T> {

    private Supplier<T> sensorSupplier;
    private T value;
    private boolean usingSupplier;


    //TODO add a counstracor that the sim and the real are the smae
    //TODO add the replay func(add replay counstracor)
    public SensorWrapper(Supplier<T> sensorSupplier, Supplier<T> sensorSimSupplier) {
        //TODO write this as if else
        this.sensorSupplier = sensorSupplier;
        usingSupplier = true; //TODO init the varubal to true
        if (!Robot.isReal()) { 
            sensorSupplier = sensorSimSupplier; //TODO should be this.sensorSupplier = sensorSimSupplier
        }
    }

    public void setValue(T value) { //TODO not sure if we need this func now base on how we use the sim 
        if (!Robot.isReal()) {
            this.value = value;
            usingSupplier = false; 
        } else {
            throw new UnsupportedOperationException("Cannot set value in real mode.");
        }
    }

    public T getValue() {
        return usingSupplier ? sensorSupplier.get() : value;
    }

    public void useSupplier() {
        usingSupplier = true;
    }


}
