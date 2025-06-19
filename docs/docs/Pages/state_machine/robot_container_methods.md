# RobotContainer Overview

The `RobotContainer` class serves as the main control hub of the robot. It manages critical robot-wide functionality such as the **robot state**, **driver/operator controllers**, **simulation configuration**, and **subsystem command coordination**.

---

##  Controller Management

The `RobotContainer` defines default controllers:

* `driverController`: Defaults to a `PS5MAController` on port `0`
* `operatorController`: Defaults to a `PS5MAController` on port `1`

You can customize or override them:

```java
setDriverController(new XboxMAController(1));
getDriverController();

setOperatorController(new PS5MAController(3));
getOperatorController();
```

---

##  Simulation Configuration

Simulation tools can be configured using the following methods:

```java
setSwerveDriveSimulation(SwerveDriveSimulation simulation);
```

Enables drivetrain simulation with MapleSim.

```java
setGamePiecesList(new String[] {"Coral", "Algae"});
```

Defines the list of simulated game pieces in MapleSim.

---

##  Robot State Management

The container manages the current and previous `RobotState`:

```java
getRobotState();
getLastRobotState();

setRobotState(RobotState.CLIMB);
```

> While `setRobotState()` can be called directly, it is generally managed by triggers and robot logic.

---

##  Subsystem Coordination

You can register subsystem commands to be scheduled automatically:

```java
addSystemCommand(new ArmCommand());
```


---

##  Lifecycle Methods

The `RobotContainer` also defines lifecycle methods that are called in thier corresponding methods in the `Robot` class. These calles are are already in place if you generated your project from our template. 

* `robotInit()`
* `robotPeriodic()`
* `teleopInit()`, `teleopPeriodic()`
* `autonomousInit()`, `autonomousPeriodic()`
* `testInit()`, `testPeriodic()`
* `simulationInit()`, `simulationPeriodic()`

