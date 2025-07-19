
package com.MAutils.Subsystems.DeafultSubsystems.IOs.PowerControlled;

import com.MAutils.Logger.MALog;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PowerSystemConstants;
import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.PowerSystemIO;

public class PowerIOReplay implements PowerSystemIO {

    private final PowerSystemConstants systemConstants;
    protected final String logPath;

    public PowerIOReplay(PowerSystemConstants systemConstants) {
        this.systemConstants = systemConstants;
        logPath = systemConstants.LOG_PATH == null ? "/Subsystems/" + systemConstants.systemName + "/IO" : systemConstants.LOG_PATH;
    }

    public double getVelocity() {
        return MALog.getReplayEntry(logPath + "/Velocity").getDouble(0);
    }

    public double getPosition() {
        return MALog.getReplayEntry(logPath + "/Position").getDouble(0);
    }

    public double getCurrent() {
        return MALog.getReplayEntry(logPath + "/Current").getDouble(0);
    }

    public double getAppliedVolts() {
        return MALog.getReplayEntry(logPath + "/Voltage").getDouble(0);
    }

    public void setVoltage(double voltage) {
    }

    public void setBrakeMode(boolean isBrake) {
    }

    @Override
    public void updatePeriodic() {
        MALog.log(logPath + "/Velocity", getVelocity());
        MALog.log(logPath + "/Voltage", getAppliedVolts());
        MALog.log(logPath + "/Current", getCurrent());
        MALog.log(logPath + "/Position", getPosition());

    }

    public boolean isMoving() {
        return getVelocity() / systemConstants.VELOCITY_FACTOR > 1;
    }

    public void restPosition(double position) {
    }

    public PowerSystemConstants getSystemConstants() {
        return systemConstants;
    }

    
}
