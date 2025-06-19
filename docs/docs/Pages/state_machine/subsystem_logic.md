# Subsystem Logic: `CAN_MOVE()`

Each subsystem in MAutils should override the `CAN_MOVE()` method to define whether the system is currently allowed to move. This mechanism is essential for safe coordination between multiple subsystems and simplifies complex inter-subsystem logic.

---

## Purpose of `CAN_MOVE()`

The `CAN_MOVE()` method returns a boolean that indicates whether the subsystem can safely perform movement-related actions.

It is used to:

* Prevent collisions between subsystems
* Coordinate manipulator safety (e.g., arm and elevator)
* Respect physical limits (e.g., soft stops, mechanical ranges)
* Detect fault states like high current draw

Always tailor `CAN_MOVE()` to the specific logic and safety requirements of each subsystem.

---

## Recommended Use Cases

###  Collision Avoidance

Check that your subsystem won’t collide with another.

###  Game Piece Safety

Ensure game pieces are secured or released only under valid conditions.

###  Spatial Boundaries

Avoid exceeding minimum or maximum mechanical limits.

###  Current Monitoring

Stop motion if current indicates a jam or stall.

---

## Recommended Pattern for Manipulator Subsystems

For systems like **intakes**, **grippers**, **shooters**, or **transfers**, we recommend having per-robot-state checks:

```java
public boolean IntakeCanMove() {
    return RobotContainer.getRobotState() == RobotConstants.INTAKE
        && Elevator.atPoint()
        && Arm.atPoint()
        && !gamePieceSensor.get();
}

public boolean ScoringCanMove() {
    return RobotContainer.getRobotState() == RobotConstants.SCORING
        && Elevator.atPoint()
        && Arm.atPoint()
        && gamePieceSensor.get()
        && Swerve.atPosition();
}

@Override
public boolean CAN_MOVE() {
    return IntakeCanMove() || ScoringCanMove();
}
```

This pattern ensures each robot state has its own movement condition logic, improving clarity and safety.



## Example for Motion Constraints (Elevator, Arm, etc.)

For subsystems that move within physical bounds:

```java
@Override
public boolean CAN_MOVE() {
    return getPosition() < ElevatorConstants.MAX_POSE
        && getPosition() > ElevatorConstants.MIN_POSE
        && Math.abs(getCurrent()) < 60;
}
```

This prevents damage from over-extension and detects jams based on current draw.




