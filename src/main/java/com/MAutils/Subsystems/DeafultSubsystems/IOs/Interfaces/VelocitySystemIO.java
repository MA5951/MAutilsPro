
package com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces;

public interface VelocitySystemIO extends PowerSystemIO {

    double getSetPoint(); 

    double getError();

    boolean atPoint();

    void setVelocity(double velocity); 

    void setPID(double kP, double kI, double kD);

    boolean isMoving();
}
