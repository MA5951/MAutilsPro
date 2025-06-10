
package com.MAutils.Subsystems.IOs.Interfaces;

public interface VelocitySystemIO extends PowerSystemIO {

    double getSetPoint(); 

    double getError();

    boolean atPoint();

    void setVelocity(double velocity); 


}
