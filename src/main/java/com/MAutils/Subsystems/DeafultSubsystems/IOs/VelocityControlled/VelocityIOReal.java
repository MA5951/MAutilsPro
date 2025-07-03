// package com.MAutils.Subsystems.DeafultSubsystems.IOs.VelocityControlled;

// import com.MAutils.CanBus.StatusSignalsRunner;
// import com.MAutils.Components.Motor;
// import com.MAutils.Logger.MALog;
// import com.MAutils.Subsystems.DeafultSubsystems.Constants.VelocitySystemConstants;
// import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.VelocitySystemIO;
// import com.ctre.phoenix6.StatusSignal;
// import com.ctre.phoenix6.configs.MotorOutputConfigs;
// import com.ctre.phoenix6.configs.TalonFXConfiguration;
// import com.ctre.phoenix6.controls.StrictFollower;
// import com.ctre.phoenix6.controls.VelocityVoltage;
// import com.ctre.phoenix6.controls.VoltageOut;
// import com.ctre.phoenix6.signals.NeutralModeValue;

// import edu.wpi.first.units.measure.Angle;
// import edu.wpi.first.units.measure.AngularVelocity;
// import edu.wpi.first.units.measure.Current;
// import edu.wpi.first.units.measure.Voltage;

// public class VelocityIOReal implements VelocitySystemIO {

//     private final VoltageOut voltageRequest = new VoltageOut(0);
//     private final VelocityVoltage velocityRequest = new VelocityVoltage(0);
//     private final TalonFXConfiguration motorConfig = new TalonFXConfiguration();
//     private final MotorOutputConfigs brakeConfig = new MotorOutputConfigs();
//     private StrictFollower follower;

//     private StatusSignal<AngularVelocity> motorVelocity;
//     private StatusSignal<Angle> motorPosition;
//     private StatusSignal<Double> motorError;
//     private StatusSignal<Double> motorSetPoint;
//     private StatusSignal<Current> motorCurrent;
//     private StatusSignal<Voltage> motorVoltage;

//     private final String logPath;
//     private final VelocitySystemConstants systemConstants;

//     public VelocityIOReal(String subsystemName, VelocitySystemConstants systemConstants) {
//         this.systemConstants = systemConstants;
//         logPath = systemConstants.LOG_PATH == null ? "/Subsystems/" + subsystemName + "/IO" : systemConstants.LOG_PATH;

//         configMotors();

//         motorVelocity = systemConstants.master.motorController.getVelocity(false);
//         motorCurrent = systemConstants.master.motorController.getStatorCurrent(false);
//         motorVoltage = systemConstants.master.motorController.getMotorVoltage(false);
//         motorPosition = systemConstants.master.motorController.getPosition(false);
//         StatusSignalsRunner.registerSignals(systemConstants.master.canBusID, motorVelocity, motorCurrent,
//                 motorVoltage, motorPosition);

//         motorConfig.MotorOutput.Inverted = systemConstants.master.invert;
//         systemConstants.master.motorController.getConfigurator().apply(motorConfig);

//         for (Motor motor : systemConstants.MOTORS) {
//             follower = new StrictFollower(systemConstants.master.canBusID.id);
//             motorConfig.MotorOutput.Inverted = motor.invert;
//             motor.motorController.getConfigurator().apply(motorConfig);
//         }

//     }

//     protected void configMotors() {
//         motorConfig.Feedback.SensorToMechanismRatio = systemConstants.GEAR;

//         motorConfig.MotorOutput.NeutralMode = systemConstants.IS_BRAKE
//                 ? NeutralModeValue.Brake
//                 : NeutralModeValue.Coast;

//         motorConfig.Slot0.kP = systemConstants.getGainConfig().Kp;
//         motorConfig.Slot0.kI = systemConstants.getGainConfig().Ki;
//         motorConfig.Slot0.kD = systemConstants.getGainConfig().Kd;
//         motorConfig.Slot0.kS = systemConstants.getGainConfig().Ks;

//         motorConfig.Voltage.PeakForwardVoltage = systemConstants.PEAK_FORWARD_VOLTAGE;
//         motorConfig.Voltage.PeakReverseVoltage = systemConstants.PEAK_REVERSE_VOLTAGE;

//         motorConfig.CurrentLimits.StatorCurrentLimit = systemConstants.STATOR_CURRENT_LIMIT;
//         motorConfig.CurrentLimits.StatorCurrentLimitEnable = systemConstants.CURRENT_LIMIT_ENABLED;
//     }

//     @Override
//     public double getCurrent() {
//         return motorCurrent.getValueAsDouble();
//     }

//     @Override
//     public double getAppliedVolts() {
//         return motorVoltage.getValueAsDouble();
//     }

//     @Override
//     public double getVelocity() {
//         return motorVelocity.getValueAsDouble() * systemConstants.VELOCITY_FACTOR;
//     }

//     @Override
//     public double getPosition() {
//         return motorPosition.getValueAsDouble() * systemConstants.POSITION_FACTOR;
//     }

//     @Override
//     public double getError() {
//         return motorError.getValueAsDouble() * systemConstants.POSITION_FACTOR;
//     }

//     @Override
//     public double getSetPoint() {
//         return motorSetPoint.getValueAsDouble() * systemConstants.POSITION_FACTOR;
//     }

//     @Override
//     public boolean atPoint() {
//         return Math.abs(getError()) < systemConstants.TOLERANCE;
//     }

//     @Override
//     public void setBrakeMode(boolean isBrake) {
//         brakeConfig.NeutralMode = isBrake
//                 ? NeutralModeValue.Brake
//                 : NeutralModeValue.Coast;

//         for (Motor motor : systemConstants.MOTORS) {
//             motorConfig.MotorOutput.Inverted = motor.invert;
//             motor.motorController.getConfigurator().apply(brakeConfig);
//         }
//     }

//     @Override
//     public void setVoltage(double volt) {
//         systemConstants.MOTORS[0].motorController.setControl(voltageRequest.withOutput(volt)
//                 .withLimitForwardMotion(Math.abs(getCurrent()) > systemConstants.MOTOR_LIMIT_CURRENT)
//                 .withLimitReverseMotion(Math.abs(getCurrent()) < systemConstants.MOTOR_LIMIT_CURRENT));
//         for (Motor motor : systemConstants.MOTORS) {
//             motor.motorController.setControl(follower);
//         }
//     }

//     @Override
//     public void setVelocity(double Velocity) {
//         if (Velocity > systemConstants.MAX_VELOCITY) {
//             Velocity = systemConstants.MAX_VELOCITY;
//             throw new IllegalArgumentException("Velocity exceeds maximum limit: " + systemConstants.MAX_VELOCITY);
//         }
//         systemConstants.MOTORS[0].motorController.setControl(velocityRequest.withVelocity(Velocity)
//                 .withSlot(0)
//                 .withLimitForwardMotion(Math.abs(getCurrent()) > systemConstants.MOTOR_LIMIT_CURRENT)
//                 .withLimitReverseMotion(Math.abs(getCurrent()) < systemConstants.MOTOR_LIMIT_CURRENT));
//         for (Motor motor : systemConstants.MOTORS) {
//             motor.motorController.setControl(follower);
//         }
//     }

//     @Override
//     public boolean isMoving() {
//         return motorVelocity.getValueAsDouble() > 1;
//     }

//     @Override
//     public void updatePeriodic() {
//         MALog.log(logPath + "/Velocity", getVelocity());
//         MALog.log(logPath + "/Voltage", getAppliedVolts());
//         MALog.log(logPath + "/Current", getCurrent());
//         MALog.log(logPath + "/Position", getPosition());
//         MALog.log(logPath + "/Set Point", getSetPoint());
//         MALog.log(logPath + "/Error", getError());
//         MALog.log(logPath + "/At Point", atPoint());

//     }

//     @Override
//     public void setPID(double kP, double kI, double kD) {
//         motorConfig.Slot0.kP = systemConstants.getGainConfig().Kp;
//         motorConfig.Slot0.kI = systemConstants.getGainConfig().Ki;
//         motorConfig.Slot0.kD = systemConstants.getGainConfig().Kd;

//         motorConfig.MotorOutput.Inverted = systemConstants.master.invert;
//         systemConstants.master.motorController.getConfigurator().apply(motorConfig);
//     }

// }
