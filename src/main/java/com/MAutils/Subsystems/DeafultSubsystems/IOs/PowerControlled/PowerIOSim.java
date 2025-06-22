package com.MAutils.Subsystems.DeafultSubsystems.IOs.PowerControlled;

import com.MAutils.Logger.MALog;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PowerSystemConstants;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.PowerSystemIO;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class PowerIOSim implements PowerSystemIO {
  private final DCMotorSim motorSim;
  private final PowerSystemConstants constants;
  private final String logPath;
  private double desiredVoltage = 0.0;

  private static final double kDampingRate = 13.0;
  private final double kDt = 0.02;//TODO

  public PowerIOSim(String systemName, PowerSystemConstants constants) {
    this.constants = constants;
    this.logPath = constants.LOG_PATH == null ? "/Subsystems/" + systemName + "/IO" : constants.LOG_PATH;

    var plant = LinearSystemId.createDCMotorSystem(
        DCMotor.getFalcon500(constants.MOTORS.length),
        constants.INERTIA,
        constants.GEAR
    );
    motorSim = new DCMotorSim(
        plant,
        DCMotor.getFalcon500(constants.MOTORS.length)
    );
  }

  @Override
  public double getVelocity() {
    return motorSim.getAngularVelocityRPM();
  }

  @Override
  public double getPosition() {
    return motorSim.getAngularPositionRotations() * constants.POSITION_FACTOR;
  }

  @Override
  public double getCurrent() {
    return motorSim.getCurrentDrawAmps();
  }

  @Override
  public double getAppliedVolts() {
    return motorSim.getInputVoltage();
  }

  @Override
  public void setVoltage(double voltage) {
    desiredVoltage = Math.max(Math.min(voltage, constants.PEAK_FORWARD_VOLTAGE), constants.PEAK_REVERSE_VOLTAGE);
  }

  @Override
  public void setBrakeMode(boolean isBrake) {
    constants.IS_BRAKE = isBrake;
  }

  @Override
  public void updatePeriodic() {
    double voltageToApply = desiredVoltage;

    if (constants.IS_BRAKE && (!DriverStation.isEnabled() || Math.abs(voltageToApply) < 1e-3)) {
      double pos = motorSim.getAngularPositionRad();
      double vel = motorSim.getAngularVelocityRadPerSec();
      double dampFactor = Math.max(0.0, 1.0 - kDampingRate * kDt);
      motorSim.setState(pos, vel * dampFactor);
      voltageToApply = 0.0;
    }

    motorSim.setInputVoltage(voltageToApply);
    motorSim.update(kDt);

    MALog.log(logPath + "/Velocity", getVelocity());
    MALog.log(logPath + "/Voltage", getAppliedVolts());
    MALog.log(logPath + "/Current", getCurrent());
    MALog.log(logPath + "/Position", getPosition());
  }
}
