
package com.MAutils.Components;

import java.util.function.Supplier;

import frc.robot.Robot;

public class SensorSimWrapper<T> {

    private Supplier<T> sensorSupplier;
    private T value;
    private boolean usingSupplier;


    public SensorSimWrapper(Supplier<T> sensorSupplier) {
        this.sensorSupplier = sensorSupplier;
        usingSupplier = true;
    }

    public void setSimSupplier(Supplier<T> sensorSimSupplier) {
        if (!Robot.isReal()) {
            sensorSupplier = sensorSimSupplier;
        }
    }

    public void setValue(T value) {
        if (!Robot.isReal()) {
            this.value = value;
            usingSupplier = false;
        }
    }

    public T getValue() {
        return usingSupplier ? sensorSupplier.get() : value;
    }

    public void useSupplier() {
        usingSupplier = true;
    }


}
