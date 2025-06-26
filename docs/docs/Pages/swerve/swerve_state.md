# SwerveState Guide

The `SwerveState` class represents a defined operational state for a swerve drive. Each state encapsulates the drive commands (chassis speeds) that the robot should use when in that state.

This abstraction makes it easier to manage complex behaviors like target locking, predefined trajectories, or driver input modes.

---

## Constructor

Each state requires a name for logging and identification:

```java
SwerveState CORAL_STATION_LOCK = new SwerveState("Coral Station Lock");
```

---

## Defining Speeds

You can configure how the state determines the robot's movement:

* `withXY(SwerveController controller)` – uses the X and Y speeds from the given controller
* `withOmega(SwerveController controller)` – uses the rotational speed (omega) from the controller
* `withSpeeds(SwerveController controller)` – uses full chassis speeds from the controller
* `withSpeeds(ChassisSpeeds speeds)` – sets constant speeds
* `withXY(double x, double y)` – sets constant X and Y speeds
* `withOmega(double omega)` – sets a constant rotational speed
* `withXY(Supplier<Double> x, Supplier<Double> y)` – sets dynamic X and Y speeds
* `withOmega(Supplier<Double> omega)` – sets dynamic rotational speed

---

## State Entry and Execution Hooks

These allow you to run logic when entering or while running a swerve state:

* `withOnStateEnter(Runnable onStateEnter)` – code to run once when the state is entered
* `withOnStateRunning(Runnable onStateRunning)` – code to run periodically while in the state

This is useful for Setting setpoints and parameters for controllers

---

## Example

Here is a complete example of defining a state that follows driver control for XY movement, but locks the robot's angle toward a target:

```java
SwerveState CORAL_STATION_LOCK = new SwerveState("Coral Station Lock");

CORAL_STATION_LOCK
    .withXY(fieldCentricDrive)
    .withOmega(angleAdjustController)
    .withOnStateEnter(() -> angleAdjustController.withSetPoint(SuperStructure.getCoralAngle()));
```

This makes the robot follow driver field-centric translation while locking rotation toward a specific target.

```java
SwerveState CORAL_STATION_LOCK = new SwerveState("Coral Station Lock");

CORAL_STATION_LOCK
    .withXY(-1,0)
    .withOmega(0);
```

This makes the robot move at -1 meter per secound in X while 0 in Y and Omega while being in this state.

```java
SwerveState CORAL_STATION_LOCK = new SwerveState("Coral Station Lock");

CORAL_STATION_LOCK
    .withSpeeds(fieldCentricDrive);
```

This makes the robot folloe the driver field-centric translation.
