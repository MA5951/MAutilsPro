# StateSubsystem Overview

`StateSubsystem` is a base class in MAutils designed to represent robot subsystems that operate in distinct states. It simplifies state-based control logic by associating the subsystem with a `SubsystemState` and `SystemMode`, and logs system behavior through MALog.

---


## Constructor

```java
public StateSubsystem(String name, SubsystemState... subsystemStates)
```

* **name** – A unique name for this subsystem (used in logging)
* **subsystemStates** – The set of states this subsystem can operate in; each state is initialized with a reference to this subsystem

---

##  Fields and Structure

Each `StateSubsystem` includes the following fields:

* **`SubsystemState currentState`** – The current state of the subsystem
* **`SystemMode systemMode`** – The system control mode (`AUTOMATIC`, `MANUAL`)
* **`String subsystemName`** – A unique name for this subsystem, used in logging and dashboards

---

##  Core Methods

These are the primary methods available in a `StateSubsystem`:

* `setState(SubsystemState state)`
  Sets the subsystem’s current state.

* `getCurrentState()`
  Returns the currently active `SubsystemState`.

* `setSystemMode(SystemMode mode)`
  Sets the system's operational mode.

* `getSystemMode()`
  Returns the current `SystemMode`.

* `CAN_MOVE()`
  Returns `true` or `false` based on whether the subsystem is allowed to operate. Override this in your subclass to implement custom logic.

* `getSelfTest()`
  Returns a `Command` that runs a self-test. This is intended to be overridden in each subsystem to provide meaningful diagnostics. See the [SelfTest documentation](#) for more details.

* `periodic()`
  Called once per scheduler loop. This method includes automatic logging of the subsystem state.

??? info "Default Logging Behavior"
    The following values are automatically logged every cycle to NetworkTables:

    ```
    - `/RobotControl/{subsystemName}/Current State`
    - `/RobotControl/{subsystemName}/System Function State`
    - `/RobotControl/{subsystemName}/Can Move`
    ```

---

##  Instantiating a StateSubsystem

When constructing your `StateSubsystem`, you must provide a name and optionally pass in all the states the subsystem can use. Each state will automatically link back to this subsystem.

```java
public class Arm extends StateSubsystem {

    public Arm() {
        super("Arm",
            ArmConstants.IDLE,
            ArmConstants.INTAKE,
            ArmConstants.EXTENDED,
            ArmConstants.RETRACTED,
            ArmConstants.FEEDER
        );
    }
}
```

This design ensures all your subsystem states are registered correctly and the name is consistent across logs and NT entries.

---

## Notes

* Works with any custom `SubsystemState` implementation.
* Designed to simplify state-machine driven control.
* Integrates seamlessly with `MALog` for tracking and diagnostics.

For best results, pair this with the `DefaultSuperStructure` class and a central robot state coordinator.

---


## SubsystemState
The SubsystemState object represents a state the a spacific subsystem can be in. It includes a stateName artabuit and a subsystem fiedl that represents the StateSubsystem that this state ia assigned to.

Calling the `setState()` dunction will set the state of the assigned subsytem to the state


```
SubsystemState IDLE = new SubsystemState("IDLE", intake);
Or:
SubsystemState IDLE = new SubsystemState("IDLE");
IDLE.setSubsystem(intake);

IDLE.setState(); //Sets the intake subsystem to the "IDLE" state