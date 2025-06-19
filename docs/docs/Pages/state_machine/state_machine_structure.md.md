# State Machine Structure

MAutils provides an advanced, flexible, and easy-to-use state machine structure designed to manage robot behavior at both the subsystem and full-system levels.

This architecture is built around clear modular components:

---

## Flow Diagram

``` mermaid
graph LR
  A[Driver Inputs, Sensors, Odometry Data, Vision Data ...] --> B[StateTriggers, Decide the current RobotState based on the data];
  B --> C[RobotState sets all of the subsystems to thier matching SubsystemStates];
  C --> D[StateSubsystem, holds the system's current SubsystemStates, Calculates CAN_MOVE and decides SystemMode based on inputs ];
  A --> D;
  D --> E[SystemCommand, runs the system based on its current SubsystemState, CAN_MOVE and SystemMode];
```
##  Key Components

### `RobotContainer` (extends `DefaultRobotContainer`)

* Central brain for the robot.
* Manages the current `RobotState`.
* Handles:
    * State transitions and logic (StateTriggers)
    * Controllers input
    * Logging setup
    * Simulation configuration 

---

### `Subsystem` (extends `StateSubsystem` or one of the `DefaultSubsystems`)

* Encapsulates the physical behavior and control logic for a single subsystem (e.g. Arm, Shooter, Intake).
* Responsible for:
    * Holding the current `SubsystemState` of the system
    * Holding the current `SystemMode` (e.g. MANUAL, AUTOMATIC)
    * Handling data logging, health checks, and CAN\_MOVE logic
    * Configuring and runing Self Tests

---

### `SubsystemCommand` (extends `SubsystemCommand`)

* A command responsible for running the subsystem based on its `SubsystemState`, `SystemMode` and `CAN_MOVE()`
* Defines:
    * What the subsystem should do in each of its states
    * What happens in manual mode
    * What happens when movement is disabled

---

### `SubsystemState`

* Represents a specific state for a single subsystem.
* Assigned to a `StateSubsystem`
* Can be reused across multiple robot states

---

### `RobotState`

* Represents the state of the full robot.
* Holds a `SubsystemState` for each subsystem.

