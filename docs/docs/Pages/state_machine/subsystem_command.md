# Subsystem Command

The `SubsystemCommand` defines the behavior of a subsystem in different operational scenarios. It centralizes logic for automatic control, manual override, and safety conditions when the subsystem cannot move.

This pattern ensures each subsystem consistently follows its intended behavior without duplicating control logic across robot states.

---

##  Command Structure

Each `SubsystemCommand` must implement the following three methods:

### 1. `Automatic()`

Handles autonomous and automatic robot modes. Typically structured with a switch statement based on the current state:

```java
public void Automatic() {
    switch (arm.getCurrentState().stateName) {
        case "IDLE":
            arm.setVoltage(arm.getFeedForward());
            break;
        case "INTAKE":
            arm.setPosition(ArmConstants.INTAKE_POS);
            break;
        case "SCORING ":
            arm.setPosition(SuperStructure.getScoringLevel().getArmPos());
            break;
        case "BALL_ANGLE":
            arm.setPosition(SuperStructure.getBallRemovingLevel().getArmPos());
            break;
    }
}
```

---

### 2. `Manual()`

Typically used to apply input from the operator controller:

```java
public void Manual() {
    arm.setVoltage(
        RobotContainer.getOperatorController().getLeftY() * ArmConstants.MANUAL_VOLTAGE_LIMIT
    );
}
```

---

### 3. `CantMove()`

Called when `CAN_MOVE()` returns `false`. This prevents unsafe movement:

```java
public void CantMove() {
    arm.setVoltage(arm.getFeedForward());
}
```

In most systems, this simply applies a feedforward hold or zero voltage.

---

##  Example: Full ArmCommand

```java
package frc.robot.commands;

import com.MAutils.RobotControl.SubsystemCommand;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Arm.Arm;

public class ArmCommand extends SubsystemCommand {
    private static Arm arm = RobotContainer.arm;

    public ArmCommand() {
        super(arm);
    }

    public void Automatic() {
    switch (arm.getCurrentState().stateName) {
        case "IDLE":
            arm.setVoltage(arm.getFeedForward());
            break;
        case "INTAKE":
            arm.setPosition(ArmConstants.INTAKE_POS);
            break;
        case "SCORING ":
            arm.setPosition(SuperStructure.getScoringLevel().getArmPos());
            break;
        case "BALL_ANGLE":
            arm.setPosition(SuperStructure.getBallRemovingLevel().getArmPos());
            break;
        }
    }

    public void Manual() {
        arm.setVoltage(
            RobotContainer.getOperatorController().getLeftY() * ArmConstants.MANUAL_VOLTAGE_LIMIT);
    }

    public void CantMove() {
        arm.setVoltage(arm.getFeedForward());
    }
}

```

---
## Autonomous and Test Modes

* Autonomous:
 By default, the command executes the Automatic() method during autonomous mode. You can override this  method if you require a different behavior in auto.

* Test:
In test mode, no logic is run by default. This prevents interference with any self-test commands being executed.
You may override this behavior by defining a custom Test() method in your command if needed.