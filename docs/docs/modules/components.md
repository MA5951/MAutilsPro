# Components Module

Provides hardware abstractions for motors, cameras, and LEDs.

## `Motor` Wrapper
```java
Motor wristMotor = new Motor(
  new TalonFX(1), DCMotor.getFalcon500(1), "Wrist", InvertedValue.CounterClockwise_Positive
);
```

## `Camera` Configs
Predefined resolutions & FOV for Limelight versions:

```java
int width = Camera.Cameras.LL3.width;
int fov   = Camera.Cameras.LL3.fov;
```
