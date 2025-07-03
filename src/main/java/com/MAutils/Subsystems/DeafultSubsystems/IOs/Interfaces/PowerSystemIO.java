
package com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces;

import com.MAutils.Subsystems.DeafultSubsystems.Constants.PowerSystemConstants;

public interface PowerSystemIO {

    double getVelocity(); 

    double getPosition();

    double getCurrent(); 

    double getAppliedVolts(); 

    void setVoltage(double voltage); 

    void setBrakeMode(boolean isBrake); 

    void updatePeriodic();

    boolean isMoving();

    void restPosition(double position);

    PowerSystemConstants getSystemConstants();

}
