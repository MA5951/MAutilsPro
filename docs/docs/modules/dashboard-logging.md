# Dashboard & Logging Module

## Notifications
Use `DashBoard.sendNotification(...)` to alert drivers.

## Data Logging
`MALog` wraps WPILib’s DataLogManager:

```java
MALog.startLog(MALogMode.TELEOP);
MALog.log("/Drive/Velocity", currentVelocity);
```

Logs appear in `/home/lvuser/logs/` on RoboRIO.
