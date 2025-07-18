
package com.MAutils.Swerve.Utils;

import edu.wpi.first.math.kinematics.ChassisSpeeds;

public abstract class SwerveController {
    private String name;
    protected ChassisSpeeds speeds = new ChassisSpeeds();

    public SwerveController(String name) {
        this.name = name;
        //TODO init speeds to zero
    }

    public abstract ChassisSpeeds getSpeeds();

    //TODO add log as abstract func

    public String getName() {
        return name;
    }

}
