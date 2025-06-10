
package com.MAutils.Subsystems.IOs.Interfaces;

public interface PositionSystemIO extends PowerSystemIO {

    double getSetPoint(); 

    double getError();

    boolean atPoint();

    void setPosition(double position); 

    void setPosition(double position, double voltageFeedForward); 

}
