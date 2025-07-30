
package com.MAutils.Swerve.Controllers;

import com.MAutils.Swerve.Utils.SwerveController;

import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class WayPointsController extends SwerveController {

    public WayPointsController() {
        super("WayPointsController");

        
    }

    public ChassisSpeeds getSpeeds() {
        return new ChassisSpeeds(0, 0, 0);
    }

    
}
