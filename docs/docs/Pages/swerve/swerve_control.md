# Swerve Controller Guide

## What is a Swerve Controller?

A **SwerveController** takes input data (like joystick positions or sensor values) and converts them into a `ChassisSpeeds` object.

All swerve controllers must extend the abstract `SwerveController` class and implement the `getSpeeds()` method.

Each controller logs its data to NetworkTables at:

```
/Subsystems/Swerve/Controllers/{Controller Name}/{Data Name}
```

---

##  Built-In Controllers

### FieldCentricDrive

* Inputs: Driver controller, gyro angle, swerve constants
* Output: Field-relative `ChassisSpeeds`
* Example:

```java
FieldCentricDrive fieldCentricDrive = new FieldCentricDrive(driverController, swerveSystem, constants);
fieldCentricDrive.withReduction(() -> driverController.getR2(), 0.4); // 40% speed when R2 pressed
.withSclers(1.0, 0.7); // X and Y scaling
```

---

### AngleAdjustController

* Inputs: Setpoint, current yaw (gyro), PIDController

* Output: Angular `omega` control in `ChassisSpeeds`


* Example:

```java
PIDController angleAdjustPID = new PIDController(0.1, 0, 0);
angleAdjustPID.enableContinuousInput(180, -180);

AngleAdjustController angleAdjustController = new AngleAdjustController(
    angleAdjustPID,
    () -> swerveSystem.getGyroData().yaw
);

angleAdjustController.withSetPoint(180);
```

---

### XYAdjustController

* Inputs: XY setpoints and current position, two PIDControllers
* Output: X and Y velocity control in `ChassisSpeeds`
* Example:

```java
xyAdjustController
    .withXYControllers(xPID, yPID)
    .withXYSuppliers(() -> pose.getX(), () -> pose.getY());
```

---

##  Create Your Own

You can implement your own `SwerveController` by overriding `getSpeeds()` and logging telemetry for visibility.

Your controller can accept any combination of sensors, PID controllers, or input sources.

