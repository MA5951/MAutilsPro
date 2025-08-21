
package com.MAutils.Components;

import java.util.function.Supplier;

import frc.robot.Robot;

public class DoubleSensorWrapper {

    private Supplier<Double> sensorSupplier;
    private Double value;
    private boolean usingSupplier;


    public DoubleSensorWrapper(Supplier<Double> sensorSupplier, Supplier<Double> sensorSimSupplier) {
        this.sensorSupplier = sensorSupplier;
        usingSupplier = true;

        if (!Robot.isReal()) {
            sensorSupplier = sensorSimSupplier;
        }
    }

    public void setValue(Double value) {
        if (!Robot.isReal()) {
            this.value = value;
            usingSupplier = false;
        } else {
            throw new UnsupportedOperationException("Cannot set value in real mode.");
        }
    }

    public Double getValue() {
        return usingSupplier ? sensorSupplier.get() : value;
    }

    public void useSupplier() {
        usingSupplier = true;
    }


}
