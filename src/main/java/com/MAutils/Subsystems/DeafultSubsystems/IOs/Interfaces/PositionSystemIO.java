
package com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces;

public interface PositionSystemIO extends PowerSystemIO {

    double getSetPoint(); 

    double getError();

    boolean atPoint();

    void setPosition(double position); 

    void setPosition(double position, double voltageFeedForward); 

    void restPosition(double position);

    void setPID(double kP, double kI, double kD);
    
    boolean isMoving();

}
