### File Structure for Subsystems

MAutils encourages a clean, modular folder and file structure to organize subsystem logic, constants, and commands.

Each **subsystem** resides in its own folder and contains two core files:

* A main subsystem class (e.g., `Intake.java`, `Arm.java`)
* A constants file (e.g., `IntakeConstants.java`, `GripperConstants.java`)
* A folder that containse IOs and other heleper class (If needed) 

This structure makes it easy to locate physical configurations, states, and PID parameters for each component.

---

##  Subsystem Folder Layout

```text
/robot/Subsystems
  └── Intake/
        ├── IOs (If needed)
        |    ├── IntakeIOReal.java
             └── IntakeIOSim.java
        ├── Intake.java
        └── IntakeConstants.java
        
  └── Arm/
        ├── Arm.java
        └── ArmConstants.java
```

---

##  Constants File Purpose

The `*Constants.java` file should include:

* Physical dimensions (gear ratios, encoder ticks, etc.)
* PID tuning values
* Static configuration parameters
* The `SubsystemState` definitions used by that subsystem

Example (partial):

```java
public class ArmConstants {
    public static final double kP = 0.07;
    public static final double ARM_LENGTH = 0.48;

    public static final SubsystemState IDLE = new SubsystemState("IDLE");
    public static final SubsystemState EXTENDED = new SubsystemState("EXTENDED");
}
```

---

##  Commands Folder

The `commands` folder holds commands that are responsible for runing the subsystems. Each file implements the `SubsystemCommand` interface for that subsystem.

### Folder Example:

```text
/robot/Commands/
  ├── IntakeCommand.java
  ├── ArmCommand.java
```
