
package com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces;

import com.MAutils.Subsystems.DeafultSubsystems.Constants.DeafultSystemConstants;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PowerSystemConstants;

public interface PowerSystemIO {


    double getVelocity(); 

    double getPosition();

    double getCurrent(); 

    double getAppliedVolts(); 

    double getRawVelocity();//RPM

    double getRawPosition();//Degrees

    void setVoltage(double voltage); 

    void setBrakeMode(boolean isBrake); 

    void setSystemConstants(PowerSystemConstants systemConstants);

    void updatePeriodic();

    default  public boolean isMoving() {
        return getRawVelocity() > DeafultSystemConstants.RPM_MOVING_THRESHOLD;
    }

    void restPosition(double position);

    PowerSystemConstants getSystemConstants();

}
