
package com.MAutils.Swerve.Controllers;

import com.MAutils.Swerve.SwerveSystem;
import com.MAutils.Swerve.SwerveSystemConstants;
import com.MAutils.Swerve.Utils.PPHolonomicDriveController;
import com.MAutils.Swerve.Utils.SwerveController;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;

public class PPController extends SwerveController {
    //TODO what about this class? why the auto code in comments?

    public PPController(SwerveSystemConstants constants, SwerveSystem swerveSystem, PPHolonomicDriveController holonomicDriveController) {
        super("PPController");

        // AutoBuilder.configure(
        //     () -> poseEstimator.getEstimatedPose(), 
        //     (pose2d) -> poseEstimator.resetPose(pose2d), 
        //     () -> swerveSystem.getChassisSpeeds(), 
        //     (speeds, feedforwards) -> setChassisSpeeds(speeds), 
        //     holonomicDriveController , 
        //     constants.getRobotConfig(), 
        //     () -> {
        //       var alliance = DriverStation.getAlliance();
        //       if (alliance.isPresent()) {
        //         return alliance.get() == DriverStation.Alliance.Red;
        //       }
        //       return false;
        //     }, new SubsystemBase("Virtual PPController Subssystem") {
     
        //     });//TODO
    }

    public void startPath(String pathName) {
        try {
            AutoBuilder.followPath(PathPlannerPath.fromPathFile(pathName)).schedule();
        } catch (Exception e) {
            DriverStation.reportError("Path " + pathName + " Not Found", false);
        }
    }


    private void setChassisSpeeds(ChassisSpeeds speeds) {
        this.speeds = speeds;
    }

    public ChassisSpeeds getSpeeds() {
        return speeds;
    }

}
