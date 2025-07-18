
package com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces;

import com.MAutils.Subsystems.DeafultSubsystems.Constants.PowerSystemConstants;

public interface PowerSystemIO {

    //TODO add offset


    double getVelocity(); 

    double getPosition();

    double getCurrent(); 

    double getAppliedVolts(); 

    void setVoltage(double voltage); 

    void setBrakeMode(boolean isBrake); 

    void updatePeriodic();
    

    boolean isMoving(); //TODO can be a default func

    void restPosition(double position);

    PowerSystemConstants getSystemConstants(); //TODO whey you need this? you pass the constants all the way from the top class 

}
