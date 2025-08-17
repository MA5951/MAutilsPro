
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

    default  public boolean isMoving() {
        return getVelocity() > 0.5;//TODO This deapunds on the VELCITY_FACTOR of the system constants (not shure what to do yet) 
    }

    void restPosition(double position);

    PowerSystemConstants getSystemConstants();

}
