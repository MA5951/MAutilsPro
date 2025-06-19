# RobotState Guide

The `RobotState` class defines the full-state of the robot, encapsulating the intended behavior of all subsystems at a given point in time (e.g. `INTAKE`, `PRE_SCORING`, `CLIMB`).



---

## Purpose

`RobotState` objects act as high-level modes that:

* Assign specific `SubsystemState`s to each subsystem
* Optionally run custom logic when the state is entered (e.g. reset counters, print debug messages)

---

## Fields

Each `RobotState` contains:

* `name` – A `String` representing the name of the robot state (e.g. `INTAKE`, `CLIMB`)
* `subsystemStates` – A collection of the `SubsystemState`s to be assigned when this state is activated
* `onStateSet` – An optional `Runnable` that executes custom logic (e.g. logging, resetting variables) when the state is set

---

## State Activation

To activate a robot state, call:

```java
RobotState.setState();
```

This will:

1. Run the `onStateSet` Runnable if it exists
2. Set each subsystem's state 

---

## Example Definition

Robot states are usually defined in the `RobotConstants` class for central access:

??? tip "RobotState Names"
    Use descriptive names like `PRE_SCORE`, `CLIMB`, or `SAFE_DEPLOY`

```java
public class RobotConstants {

    public static final RobotState IDLE = new RobotState(
        "IDLE",
        IntakeConstants.IDLE,
        GripperConstants.IDLE,
        ArmConstants.IDLE,
        ClimbConstants.IDLE,
        ElevatorConstants.IDLE
    );

    public static final RobotState PRE_CLIMB = new RobotState(
        "PRE_CLIMB",
        () -> { System.out.println("PRE_CLIMB Entered"); },
        IntakeConstants.IDLE,
        GripperConstants.IDLE,
        ArmConstants.CLMB_POS,
        ClimbConstants.DEPLOY,
        ElevatorConstants.CLMB_POS
    );

    public static final RobotState CLIMB = new RobotState(
        "CLIMB",
        () -> { System.out.println("Started Climb"); },
        IntakeConstants.IDLE,
        GripperConstants.IDLE,
        ArmConstants.CLMB_POS,
        ClimbConstants.CLIMB,
        ElevatorConstants.CLMB_POS
    );
}
```

Each robot state manages the coordination of all major mechanisms for a given scenario.

