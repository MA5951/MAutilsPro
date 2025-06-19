# Robot State Triggers

Robot state triggers are responsible for changing the current `RobotState` based on sensor input, subsystem conditions, or driver/operator commands.

This system helps manage transitions between complex behaviors (e.g. INTAKE ➔ IDLE ➔ SCORING) in a clear and modular way.

---

##  Trigger Overview

Each `RobotState` can have:

* **Enter triggers**: Conditions to switch *into* a state
* **Exit triggers**: Conditions to switch *out* of a state

### Example

```java
// Enter INTAKE state
!SuperStructure.hasGamePiece() && getDriverController().getL1()

// Exit INTAKE and enter IDLE when game piece is detected
getRobotState() == RobotConstants.INTAKE && SuperStructure.hasGamePiece()
```

---

##  StateTrigger Class

To simplify writing these conditions, MAutils provides the `StateTrigger` (commonly used via a static function `T(...)`).

### Syntax

```java
T(BooleanSupplier condition, RobotState stateToSet).build();
```

You can also chain additional context conditions:

```java
.withInRobotState(RobotState robotInState)
.withRobotMode(RobotMode mode)
```

These allow a trigger to only apply in specific robot states or match certain robot modes (e.g. TELEOP, AUTO).

### Full Example

```java
// Transition to INTAKE if we don't have a game piece and L1 is pressed
T(() -> !SuperStructure.hasGamePiece() && getDriverController().getL1(), RobotConstants.INTAKE)
    .build();

// Transition to IDLE once a game piece is acquired (but only if we're currently in INTAKE)
T(() -> SuperStructure.hasGamePiece(), RobotConstants.IDLE)
    .withInRobotState(RobotConstants.INTAKE)
    .build();
```

