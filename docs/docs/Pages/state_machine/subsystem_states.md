# SubsystemState

The `SubsystemState` class represents a specific state of a given subsystem.

---

##  Structure

Each subsystem has its own collection of `SubsystemState` objects, which are linked to the subsystem they belong to.

Every `SubsystemState` contains:

* `stateName` — The name of the state.
* `subsystem` — The `StateSubsystem` it is assigned to.

A `SubsystemState` can be created in two ways:

```java
public static final SubsystemState PRE_SCORING = new SubsystemState("PRE_SCORING", arm);
```

or by assigning the subsystem later:

```java
public static final SubsystemState PRE_SCORING = new SubsystemState("PRE_SCORING");
PRE_SCORING.setSubsystem(arm);
```

> ⚠️ **Warning:** Always ensure the `stateName` matches the variable name.
> This ensures consistency and improves readability when debugging or logging.

??? tip "SubsystemState Names"
    We recommand to name your subsystem states based on the robot state they will be used on or a descriptive actions, for example "INTAKE" "PRE_SCORING" "DEPLOY_CLIMB" "CLOSE_CLIMB" and so on!
---

##  Recommended Usage

It is recommended to define all your states in a constants file like this:

```java
public class ArmConstants {
    //Other Constants...

    public static final SubsystemState IDLE = new SubsystemState("IDLE");
    public static final SubsystemState INTAKE = new SubsystemState("INTAKE");
    public static final SubsystemState L1_SCORING = new SubsystemState("L1_SCORING");
    public static final SubsystemState PRE_SCORING = new SubsystemState("PRE_SCORING");
}
```

Then, pass those states into the `StateSubsystem` constructor to automatically bind them to the subsystem:

```java
public class Arm extends StateSubsystem {
    public Arm() {
        super("Arm",
            ArmConstants.IDLE,
            ArmConstants.INTAKE,
            ArmConstants.L1_SCORING,
            ArmConstants.PRE_SCORING
        );
    }
}
```

This makes the state machine setup cleaner and ensures every state knows which subsystem it belongs to.

