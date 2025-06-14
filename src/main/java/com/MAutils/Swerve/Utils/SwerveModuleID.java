
package com.MAutils.Swerve.Utils;

import com.MAutils.Utils.CANBusID;


public class SwerveModuleID {

    public final CANBusID driveMotor;
    public final CANBusID steerMotor;
    public final int steerEncoderID;

    public SwerveModuleID(CANBusID driveMotor, CANBusID steerMotor, int steerEncoderID) {
        this.driveMotor = driveMotor;
        this.steerMotor = steerMotor;
        this.steerEncoderID = steerEncoderID;
    }

}
