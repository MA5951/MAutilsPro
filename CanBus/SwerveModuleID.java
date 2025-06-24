
package com.MAutils.CanBus;


public class SwerveModuleID {

    public final CANBusID driveMotor;
    public final CANBusID steerMotor;
    public final CANBusID steerEncoder;

    public SwerveModuleID(CANBusID driveMotor, CANBusID steerMotor, CANBusID steerEncoder) {
        this.driveMotor = driveMotor;
        this.steerMotor = steerMotor;
        this.steerEncoder = steerEncoder;
    }

}
