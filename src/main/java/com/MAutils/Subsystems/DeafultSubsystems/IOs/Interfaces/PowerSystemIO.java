
package com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces;


public interface PowerSystemIO {

    double getVelocity(); 

    double getPosition();

    double getCurrent(); 

    double getAppliedVolts(); 

    void setVoltage(double voltage); 

    void setBrakeMode(boolean isBrake); 

    void updatePeriodic();

}
