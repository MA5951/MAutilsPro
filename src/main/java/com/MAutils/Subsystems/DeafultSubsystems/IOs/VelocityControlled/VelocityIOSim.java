// package com.MAutils.Subsystems.DeafultSubsystems.IOs.VelocityControlled;

// import static edu.wpi.first.units.Units.RotationsPerSecond;

// import com.MAutils.Logger.MALog;
// import com.MAutils.Subsystems.DeafultSubsystems.Constants.VelocitySystemConstants;
// import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.VelocitySystemIO;
// import com.MAutils.Utils.DeafultRobotConstants;

// import edu.wpi.first.math.controller.PIDController;
// import edu.wpi.first.math.system.plant.DCMotor;
// import edu.wpi.first.math.system.plant.LinearSystemId;
// import edu.wpi.first.wpilibj.DriverStation;
// import edu.wpi.first.wpilibj.simulation.DCMotorSim;

// public class VelocityIOSim implements VelocitySystemIO {
//     private final DCMotorSim motorSim;
//     private final VelocitySystemConstants constants;
//     private final PIDController pid;
//     private double voltage;

//     private static final double kDampingRate = 13.0;
//     private final String logPath;

//     public VelocityIOSim(String systemName, VelocitySystemConstants constants) {
//         this.constants = constants;
//         this.logPath = constants.LOG_PATH == null ? "/Subsystems/" + systemName + "/IO" : constants.LOG_PATH;

//         var plant = LinearSystemId.createDCMotorSystem(
//                 DCMotor.getKrakenX60(constants.MOTORS.length),
//                 constants.INERTIA,
//                 constants.GEAR);
//         motorSim = new DCMotorSim(
//                 plant,
//                 DCMotor.getKrakenX60(constants.MOTORS.length));

//         var gains = constants.getGainConfig();
//         pid = new PIDController(gains.Kp, gains.Ki, gains.Kd);
//         pid.setTolerance(constants.TOLERANCE / constants.VELOCITY_FACTOR);

//     }

//     @Override
//     public double getVelocity() {
//         return motorSim.getAngularVelocity().in(RotationsPerSecond) * constants.VELOCITY_FACTOR;
//     }

//     @Override
//     public double getPosition() {
//         return motorSim.getAngularPositionRotations() * constants.POSITION_FACTOR;
//     }

//     @Override
//     public double getCurrent() {
//         return motorSim.getCurrentDrawAmps();
//     }

//     @Override
//     public double getAppliedVolts() {
//         return motorSim.getInputVoltage();
//     }

//     @Override
//     public void setVoltage(double voltage) {
//         motorSim.setInputVoltage(voltage);
//     }

//     @Override
//     public void setBrakeMode(boolean isBrake) {
//         constants.IS_BRAKE = isBrake;
//     }

//     @Override
//     public double getSetPoint() {
//         return pid.getSetpoint() * constants.VELOCITY_FACTOR;
//     }

//     @Override
//     public double getError() {
//         return pid.getPositionError() * constants.VELOCITY_FACTOR;
//     }

//     @Override
//     public boolean atPoint() {
//         return pid.atSetpoint();
//     }

//     @Override
//     public void setVelocity(double velocity) {
//         if (velocity > constants.MAX_VELOCITY) {
//             velocity = constants.MAX_VELOCITY;
//             System.out.println("Velocity exceeds maximum limit, setting to " + constants.MAX_VELOCITY); // TODO
//                                                                                                         // normal
//         }
//         pid.setSetpoint(velocity / constants.VELOCITY_FACTOR);
//         voltage = pid.calculate(motorSim.getAngularVelocity().in(RotationsPerSecond))
//                 + ((velocity / constants.MAX_VELOCITY) * constants.PEAK_FORWARD_VOLTAGE);

//         if ((voltage > 0 && getCurrent() > constants.MOTOR_LIMIT_CURRENT)
//                 || (voltage < 0 && getCurrent() < constants.MOTOR_LIMIT_CURRENT)) {
//             voltage = 0;
//         }

//         voltage = Math.min(voltage, constants.PEAK_REVERSE_VOLTAGE);
//         voltage = Math.max(voltage, constants.PEAK_FORWARD_VOLTAGE);

//         setVoltage(voltage);
//     }

//     @Override
//     public void updatePeriodic() {

//         // Simulate brake mode
//         if (constants.IS_BRAKE && (!DriverStation.isEnabled() || Math.abs(getAppliedVolts()) < 1e-3)) {
//             motorSim.setState(motorSim.getAngularPositionRad(),
//                     Math.max(0.0, 1.0 - kDampingRate * DeafultRobotConstants.kD) * motorSim.getAngularVelocityRadPerSec());
//         }
//         motorSim.update(DeafultRobotConstants.kD);

//         // Logging
//         MALog.log(logPath + "/Velocity", getVelocity());
//         MALog.log(logPath + "/Voltage", getAppliedVolts());
//         MALog.log(logPath + "/Current", getCurrent());
//         MALog.log(logPath + "/Position", getPosition());
//         MALog.log(logPath + "/Set Point", getSetPoint());
//         MALog.log(logPath + "/Error", getError());
//         MALog.log(logPath + "/At Point", atPoint());

//     }
// }
