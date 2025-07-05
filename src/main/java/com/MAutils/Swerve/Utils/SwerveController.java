
package com.MAutils.Swerve.Utils;

import edu.wpi.first.math.kinematics.ChassisSpeeds;

public abstract class SwerveController {

    private String name;
    protected ChassisSpeeds speeds = new ChassisSpeeds();

    public SwerveController(String name) {
        this.name = name;
    }

    public abstract ChassisSpeeds getSpeeds();

    public String getName() {
        return name;
    }

}
