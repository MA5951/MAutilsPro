# Advanced Usage

## PathPlanner Integration
Load and preview autonomous routines with `AutoSelector`:

```java
AutoOption[] options = new AutoOption[]{
  new AutoOption("Pick and Place", "PickPlacePath", new Pose2d(1,1, new Rotation2d())),
  new AutoOption("Balance Only", balanceCommand, new Pose2d())
};

AutoSelector selector = new AutoSelector(robot::getPose);
selector.setAutoOptions(options, true);
```

The preview appears on the on-board field widget.
