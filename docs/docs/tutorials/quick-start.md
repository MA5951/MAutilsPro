# Quick Start Tutorial

Build a simple drive-train subsystem using velocity control.

```java
// 1. Define constants
double wheelRadius = 0.075;
VelocitySystemConstants driveConstants = new VelocitySystemConstants()
  .withMotors(new Motor[]{frontLeft, frontRight, backLeft, backRight})
  .withPID(0.1, 0, 0, 5)
  .withFeedForward(0.2, 1.5)
  .withMaxVelocity(500);

// 2. Create system
VelocityControlledSystem drive = new VelocityControlledSystem(
  "SwerveDrive",
  driveConstants,
  new SubsystemState("Stopped"),
  new SubsystemState("Driving")
);

// 3. Bind to commands
Command moveForward = new RunCommand(
  () -> drive.setVelocity(300), drive
);

CommandScheduler.getInstance().setDefaultCommand(drive, moveForward);
```

Result: your robot drives at 300 RPM when enabled.
