
package com.MAutils.Swerve.Utils;

import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class SwerveController {

    private String name;

    public SwerveController(String name) {//TODO cahnge to have deafult speeds and speeds go through this object
        this.name = name;
    }


    public ChassisSpeeds getSpeeds() {
        return null;
    }

    public String getName() {
        return name;
    }

}
