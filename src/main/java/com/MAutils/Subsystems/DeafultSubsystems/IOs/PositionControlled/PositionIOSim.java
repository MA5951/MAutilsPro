
package com.MAutils.Subsystems.DeafultSubsystems.IOs.PositionControlled;

import com.MAutils.Logger.MALog;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PositionSystemConstants;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.PositionSystemIO;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class PositionIOSim implements PositionSystemIO {

    private final DCMotorSim motorSim;
    private final PositionSystemConstants systemConstants;
    private final PIDController pidController;
    private final ProfiledPIDController profiledPIDController;

    private final double kDampingRate = 13;// Used to simulate brake mode

    private String logPath;

    public PositionIOSim(String systemName, PositionSystemConstants systemConstants) {
        this.systemConstants = systemConstants;
        logPath = systemConstants.LOG_PATH == null ? "/Subsystems/" + systemName + "/IO" : systemConstants.LOG_PATH;

        motorSim = new DCMotorSim(
                LinearSystemId.createDCMotorSystem(DCMotor.getKrakenX60(systemConstants.MOTORS.length),
                        systemConstants.INERTIA, systemConstants.GEAR),
                DCMotor.getKrakenX60(systemConstants.MOTORS.length) );

        pidController = new PIDController(systemConstants.getGainConfig().Kp, systemConstants.getGainConfig().Ki,
                systemConstants.getGainConfig().Kd);

        profiledPIDController = new ProfiledPIDController(systemConstants.getGainConfig().Kp,
                systemConstants.getGainConfig().Ki, systemConstants.getGainConfig().Kd,
                new Constraints(systemConstants.CRUISE_VELOCITY, systemConstants.ACCELERATION));

                setBrakeMode(false);

    }

    public double getVelocity() {
        return motorSim.getAngularVelocityRPM();
    }

    public double getPosition() {
        return motorSim.getAngularPositionRotations() * systemConstants.POSITION_FACTOR;
    }

    public double getCurrent() {
        return motorSim.getCurrentDrawAmps();
    }

    public double getAppliedVolts() {
        return motorSim.getInputVoltage();
    }

    public void setVoltage(double voltage) {
        motorSim.setInputVoltage(voltage);
    }

    public void setBrakeMode(boolean isBrake) {
        systemConstants.IS_BRAKE = isBrake;
    }

    public double getSetPoint() {
        return pidController.getSetpoint() * systemConstants.POSITION_FACTOR;
    }

    public double getError() {
        return pidController.getPositionError() * systemConstants.POSITION_FACTOR;
    }

    public boolean atPoint() {
        return Math.abs(getError()) < systemConstants.TOLERANCE;
    }

    public void setPosition(double position) {
        if (systemConstants.IS_MOTION_MAGIC) {
            profiledPIDController.setGoal(position / systemConstants.POSITION_FACTOR);
            setVoltage(profiledPIDController.calculate(getPosition() / systemConstants.POSITION_FACTOR));
        } else {
            pidController.setSetpoint(position / systemConstants.POSITION_FACTOR);
            setVoltage(pidController.calculate(getPosition() / systemConstants.POSITION_FACTOR));
        }
    }

    public void setPosition(double position, double voltageFeedForward) {
        if (systemConstants.IS_MOTION_MAGIC) {
            profiledPIDController.setGoal(position / systemConstants.POSITION_FACTOR);
            setVoltage(profiledPIDController.calculate(getPosition() / systemConstants.POSITION_FACTOR)
                    + voltageFeedForward);
        } else {
            pidController.setSetpoint(position / systemConstants.POSITION_FACTOR);
            setVoltage(pidController.calculate(getPosition() / systemConstants.POSITION_FACTOR)
                    + voltageFeedForward);
        }
    }

    public void updatePeriodic() {

        //Simulate brake mode
        if (systemConstants.IS_BRAKE && (!DriverStation.isEnabled() || Math.abs(getAppliedVolts()) < 1e-3)) {
            motorSim.setState(motorSim.getAngularPositionRad(),
                    Math.max(0.0, 1.0 - kDampingRate * 0.02) * motorSim.getAngularVelocityRadPerSec());
        }
        motorSim.update(0.02);

        MALog.log(logPath + "/Velocity", getVelocity());
        MALog.log(logPath + "/Voltage", getAppliedVolts());
        MALog.log(logPath + "/Current", getCurrent());
        MALog.log(logPath + "/Position", getPosition());
        MALog.log(logPath + "/Set Point", getSetPoint());
        MALog.log(logPath + "/Error", getError());
        MALog.log(logPath + "/At Point", atPoint());
    }

}
