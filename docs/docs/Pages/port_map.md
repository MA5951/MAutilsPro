# Port Map

The **PortMap** file centralizes all hardware port assignments across the robot. This makes it easy to manage and configure device ports in one location.

---

## Structure

Each subsystem is given its own nested class inside the `PortMap`. All fields should be declared `public static` for easy access throughout the codebase.

The `PortMap` class should extend `DefaultPortMap`, which automatically sets up support for CAN bus access via:

* `Rio` – The RoboRIO CAN bus
* `Canivore` – The CTRE CANivore bus (if connected)

---

##  CAN Device IDs

To represent devices on the CAN bus, use the `CANBusID` class:

```java
public CANBusID(int id, CANBus bus)
```

* `id`: the device ID on the bus
* `bus`: the `CANBus` instance (e.g. `Rio` or `Canivore`)

---

##  Swerve Module IDs

For organizing swerve modules, use the `SwerveModuleID` class:

```java
public SwerveModuleID(CANBusID driveMotor, CANBusID steerMotor, CANBusID steerEncoder)
```

This bundles the three components of each swerve module. Store them in a `SwerveModuleID[]` array.

---

##  Other Device Ports

For other devices (DIO, Analog, PWM), use `int` or the appropriate type.

---

##  Example PortMap

```java
public class PortMap extends DefaultPortMap {

    public static class Swerve {

        private static final CANBusID LEFT_FRONT_ENCODER = new CANBusID(22, Canivore);
        private static final CANBusID LEFT_FRONT_DRIVE = new CANBusID(8, Canivore);
        private static final CANBusID LEFT_FRONT_TURNING = new CANBusID(5, Canivore);

        private static final CANBusID LEFT_BACK_ENCODER = new CANBusID(21, Canivore);
        private static final CANBusID LEFT_BACK_DRIVE = new CANBusID(4, Canivore);
        private static final CANBusID LEFT_BACK_TURNING = new CANBusID(9, Canivore);

        private static final CANBusID RIGHT_FRONT_ENCODER = new CANBusID(23, Canivore);
        private static final CANBusID RIGHT_FRONT_DRIVE = new CANBusID(7, Canivore);
        private static final CANBusID RIGHT_FRONT_TURNING = new CANBusID(6, Canivore);

        private static final CANBusID RIGHT_BACK_ENCODER = new CANBusID(24, Canivore);
        private static final CANBusID RIGHT_BACK_DRIVE = new CANBusID(2, Canivore);
        private static final CANBusID RIGHT_BACK_TURNING = new CANBusID(3, Canivore);

        public static final CANBusID PIGEON2 = new CANBusID(12, Canivore);

        public static final SwerveModuleID[] SWERVE_MODULE_IDS = {
            new SwerveModuleID(LEFT_FRONT_ENCODER, LEFT_FRONT_DRIVE, LEFT_FRONT_TURNING),
            new SwerveModuleID(LEFT_BACK_ENCODER, LEFT_BACK_DRIVE, LEFT_BACK_TURNING),
            new SwerveModuleID(RIGHT_FRONT_ENCODER, RIGHT_FRONT_DRIVE, RIGHT_FRONT_TURNING),
            new SwerveModuleID(RIGHT_BACK_ENCODER, RIGHT_BACK_DRIVE, RIGHT_BACK_TURNING)
        };
    }

    public static class Arm {
        public static final CANBusID ARM_MOTOR = new CANBusID(26, Canivore);
        public static final CANBusID ARM_ENCODER = new CANBusID(27, Canivore);
        public static final int MAX_LIMIT_SWITCH = 3;
    }

    public static class Intake {
        public static final CANBusID INTAKE_LEFT = new CANBusID(29, Canivore);
        public static final CANBusID INTAKE_RIGHT = new CANBusID(30, Canivore);
        public static final int FRONT_BEAM_BRAKE = 4;
        public static final int REAR_BEAM_BRAKE = 5;
    }
}
```

