
package com.MAutils.Subsystems.DeafultSubsystems.IOs.PositionControlled;

import com.MAutils.Logger.MALog;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PositionSystemConstants;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.PositionSystemIO;
import com.MAutils.Utils.DeafultRobotConstants;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class PositionIOSim extends PositionIOReal {

    private final TalonFXSim;

    private final double kDampingRate = 13;

    private double voltage;

    private final String logPath;

    public PositionIOSim(String systemName, PositionSystemConstants systemConstants) {
        super(, systemConstants)
        TalonFXSim = new TalonFXSim(systemConstants.MOTOR.getMotorController().getDeviceID(), Signal);

    }

    public void updatePeriodic() {

        // Simulate brake mode
        if (systemConstants.IS_BRAKE && (!DriverStation.isEnabled() || Math.abs(getAppliedVolts()) < 1e-3)) {
            motorSim.setState(motorSim.getAngularPositionRad(),
                    Math.max(0.0, 1.0 - kDampingRate * DeafultRobotConstants.kD) * motorSim.getAngularVelocityRadPerSec());
        }
        motorSim.update(DeafultRobotConstants.kD);

        MALog.log(logPath + "/Velocity", getVelocity());
        MALog.log(logPath + "/Voltage", getAppliedVolts());
        MALog.log(logPath + "/Current", getCurrent());
        MALog.log(logPath + "/Position", getPosition());
        MALog.log(logPath + "/Set Point", getSetPoint());
        MALog.log(logPath + "/Error", getError());
        MALog.log(logPath + "/At Point", atPoint());
    }

}
