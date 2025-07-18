
package com.MAutils.Swerve.Controllers;

import com.MAutils.Swerve.SwerveSystem;
import com.MAutils.Swerve.Utils.SwerveController;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public class ModulesAngleController extends SwerveController {

    //TODO we should think about make all the controller static 
    
    private final SwerveSystem swerveSystem;
    private SwerveModuleState[] swerveStates;

    public ModulesAngleController(SwerveSystem swerveSystem) {
        super("ModulesAngleController");
        this.swerveSystem = swerveSystem;
        this.swerveStates = new SwerveModuleState[4];

    }


    public void setAngles(Rotation2d fl, Rotation2d fr, Rotation2d bl, Rotation2d br) {
        //TODO donk like that you need to do set angles and get speeds mayby pass the rotation as a suplier
        swerveStates[0] = new SwerveModuleState(0, fl);
        swerveStates[1] = new SwerveModuleState(0, fr);
        swerveStates[2] = new SwerveModuleState(0, bl);
        swerveStates[3] = new SwerveModuleState(0, br);
    } 


    @Override
    public ChassisSpeeds getSpeeds() {
        swerveSystem.runSwerveStates(swerveStates);

        return speeds;
    }

    

}
