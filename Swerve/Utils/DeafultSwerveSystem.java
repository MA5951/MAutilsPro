
package com.MAutils.Swerve.Utils;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DeafultSwerveSystem extends SubsystemBase {

    private SwerveState currentState;

    public DeafultSwerveSystem() {
    
    }

    public void setState(SwerveState state) {
        this.currentState = state;
    }

    public SwerveState getState() {
        return currentState;
    }

    public void drive(ChassisSpeeds speeds) {
        System.out.println("Drive method not implemented!");
    }

    public void periodic() {
        
    }

}
