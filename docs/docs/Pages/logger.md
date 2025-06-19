# MALog Logging System

MALog is the unified logging utility in MAutils. It provides an interface for real-time logging to NetworkTables, as well as persistent storage via DataLogManager.


The logger records all NetworkTables data, including values logged via MALog, joystick states, vision coprocessor output, and FMS data.

---

## Initialization

Start the logger once at the beginning of a mode:

```java
MALog.startLog(MALogMode.AUTO);
// Or:
MALog.startLog(MALogMode.TELEOP);
```

This starts data logging with a unique session ID and a timestamped filename (if not running on FMS). If using `DeafultRobotContainer`, the log is started and stopped automaticly.

??? info "Log naming format"
    Logs are saved using the format: `MALog_Mode_SessionID_Timestamp`.
    \- **Mode** corresponds to the robot's current mode (`AUTO`, `TELEOP`, `TEST`).
    \- **SessionID** is a unique, persistent number that increments on each run (stored in `/home/lvuser/malog/lastLogID.txt`). Use `resetID()` to reset it manually.
    \- **Timestamp** follows the format `yyyy-MM-dd_HH-mm-ss`.

    **Example:**
    ```
    MALog_Teleop_0047_2025-11-02_16-22-17
    ```


To stop logging:

```java
MALog.stopLog();
```


---

## Logging Basic Values

You can log values using a string key:

```java
MALog.log("/Subsystems/Arm/Arm Angle", 45.0);
MALog.log("/Subsystems/Swerve/Is Aligned", true);
MALog.log("/RobotControl/Mode", "AUTO");
```

Or with suppliers (evaluated at runtime):

```java
MALog.log("/Pneumatics/Pressure", () -> compressor.getPressure());
```

---

## Logging Advanced Types

### Pose2d

```java
MALog.log("Odometry/Pose2d", drivetrain.getPose());
```

### Pose3d

```java
MALog.log("Odometry/Pose3d", camera.getPose());
```

### Pose3d\[]

```java
MALog.log("Vision/Targets", allDetectedTags);
```

### SwerveModuleState\[]

```java
MALog.logSwerveModuleStates("Drive/Modules", swerve.getModuleStates());
```

### ChassisSpeeds

```java
MALog.log("Drive/ChassisSpeeds", kinematics.toChassisSpeeds());
```

All of these publish to NetworkTables under the `MALog/` namespace.

---

## Getting Logged Values

You can retrieve numeric values from NetworkTables:

```java
double shooterRPM = MALog.getEntry("Shooter/RPM").getDouble(0);
```

---

## Flags and Status

Use `flag()` to mark specific events in logs:

```java
MALog.flag("Driver Flag");
```

You can view the flag field in a graph to easily locate these events.

??? tip "Using flags during driver practice"
    If the driver encounters a bug or something feels off, they can press a controller button mapped to `MALog.flag()`. This creates a visible marker in the log, allowing the software team to easily investigate it afterward.

Indicates the status of the robot, other parts of MAutils use this field so its mostly reserved for library use.

```java
MALog.addStatus("Self Testing");
```


---

## Example

```java
@Override
public void robotInit() {
    MALog.startLog(MALogMode.TELEOP);
}

@Override
public void teleopPeriodic() {
    MALog.log("Shooter/Velocity", shooter.getVelocity());
    MALog.log("Drive/Pose2d", drivetrain.getPose());
    MALog.log("Drive/Modules", drivetrain.getModuleStates());
    MALog.log("Intake/Pressed", intakeButton::get);
}
```