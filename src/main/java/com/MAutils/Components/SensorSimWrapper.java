
package com.MAutils.Components;

import java.util.function.Supplier;

import frc.robot.Robot;

public class SensorSimWrapper<T> {//Todo "SensorWrapper?"

    private Supplier<T> sensorSupplier;
    private T value;
    private boolean usingSupplier;


    public SensorSimWrapper(Supplier<T> sensorSupplier, Supplier<T> sensorSimSupplier) {
        this.sensorSupplier = sensorSupplier;
        usingSupplier = true;

        if (!Robot.isReal()) {
            sensorSupplier = sensorSimSupplier;
        }
    }

    public void setValue(T value) {
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
