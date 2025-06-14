
package com.MAutils.Swerve;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.MAutils.Swerve.IOs.PhoenixOdometryThread;
import com.MAutils.Swerve.IOs.Gyro.Gyro;
import com.MAutils.Swerve.IOs.SwerveModule.SwerveModule;
import com.MAutils.Swerve.Utils.SwerveSetpoint;
import com.MAutils.Swerve.Utils.SwerveSetpointGenerator;
import com.MAutils.Swerve.Utils.SwerveSystem;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public class SwerveSubsystem extends SwerveSystem {

    public static final Lock odometryLock = new ReentrantLock();
    private final SwerveConstants swerveConstants;
    private final SwerveModule[] swerveModules;// FL FR RL RR
    private final Gyro gyro;
    private SwerveSetpoint currentSetpoint = new SwerveSetpoint(
            new ChassisSpeeds(),
            new SwerveModuleState[] {
                    new SwerveModuleState(),
                    new SwerveModuleState(),
                    new SwerveModuleState(),
                    new SwerveModuleState()
            });
    private final SwerveSetpointGenerator swerveSetpointGenerator;

    public SwerveSubsystem(SwerveConstants swerveConstants) {
        this.swerveConstants = swerveConstants;

        swerveModules = swerveConstants.getModules();
        gyro = swerveConstants.getGyro();

        swerveSetpointGenerator = new SwerveSetpointGenerator(swerveConstants.kinematics,
                swerveConstants.modulesLocationArry);

        PhoenixOdometryThread.getInstance(swerveConstants).start();
    }

    public void periodic() {
        super.periodic();

        odometryLock.lock();
        gyro.update();
        for (var module : swerveModules) {
            module.update();
        }
        odometryLock.unlock();

        

    }
}
