# Sensor Simulation Wrapper

MAutils provides the `SensorSimWrapper` class to make sensor simulation seamless and clean. It allows you to inject data into sensors during simulation, while preserving normal behavior on a real robot.

---

## Overview

The `SensorSimWrapper` class wraps a sensor's data source and lets you override that data while in simulation.

It is a generic class (`<T>`), making it compatible with any data type such as:

* `Boolean` (e.g. DIO switches)
* `Double` (e.g. encoders, IMUs)
* Custom sensor types

---

## Constructor

```java
public SensorSimWrapper<T>(Supplier<T> sensorData);
```

This sets the default data source. For example, wrapping a limit switch:

```java
DigitalInput limitSwitch = new DigitalInput(0);
SensorSimWrapper<Boolean> limitSwitchSim = new SensorSimWrapper<>(() -> limitSwitch.get());
```

---

## Methods

### `setSimSupplier(Supplier<T> sensorSimSupplier)`

Defines the simulation supplier. This is only used during simulation and provides dynamic sensor behavior.

### `setValue(T value)`

Force-sets a value to be returned during simulation, overriding the sim supplier. Useful for precise test cases.

### `useSupplier()`

Restores the sim supplier as the active data source, stopping the override.

### `getValue()`

Returns the current value. Automatically returns real sensor data when not in simulation.

---

## Example: Simulating an Absolute Encoder

```java
public class Arm extends PositionControlledSystem {

    private Encoder absEncoder;
    private SensorSimWrapper<Double> encoderSim;

    public Arm() {
        super("Arm", ArmConstants.ARM_CONSTANTS,
            ArmConstants.IDLE, ArmConstants.UP, ArmConstants.DOWN);

        absEncoder = new Encoder(PortMap.ARM.ABS_ENCODER);

        encoderSim = new SensorSimWrapper<>(() -> absEncoder.get());
        encoderSim.setSimSupplier(() -> getPosition());
    }

    public double getAbsAngle() {
        return encoderSim.getValue();
    }
}
```


In this example, the absolute encoder is simulated by returning the arm's position when running in simulation.
