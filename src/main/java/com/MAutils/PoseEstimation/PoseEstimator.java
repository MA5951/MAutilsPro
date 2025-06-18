
package com.MAutils.PoseEstimation;

import com.MAutils.Swerve.SwerveConstants;
import com.MAutils.Swerve.IOs.Gyro.GyroIO.GyroData;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

public class PoseEstimator {

    protected final SwerveDrivePoseEstimator swervePoseEstimator;


    public PoseEstimator(SwerveConstants constants,GyroData gyroData) {
        swervePoseEstimator = new SwerveDrivePoseEstimator(constants.kinematics, Rotation2d.fromDegrees(gyroData.yaw), null, null);
    }



    public void addSwerveData(SwerveModulePosition[] swerveModulePositions, Rotation2d gyroAngle, double timestamp) {
        swervePoseEstimator.updateWithTime(timestamp, gyroAngle, swerveModulePositions);
    }




}
