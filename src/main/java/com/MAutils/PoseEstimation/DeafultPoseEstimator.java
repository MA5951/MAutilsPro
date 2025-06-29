
package com.MAutils.PoseEstimation;

import com.MAutils.Swerve.SwerveSystemConstants;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

public class DeafultPoseEstimator {

    protected final SwerveDrivePoseEstimator swervePoseEst0imator;

    public DeafultPoseEstimator(SwerveSystemConstants constants) {
        swervePoseEstimator = new SwerveDrivePoseEstimator(constants.kinematics, Rotation2d.fromDegrees(0),
                new SwerveModulePosition[] { new SwerveModulePosition(0, Rotation2d.kZero),
                        new SwerveModulePosition(0, Rotation2d.kZero), new SwerveModulePosition(0, Rotation2d.kZero),
                        new SwerveModulePosition(0, Rotation2d.kZero) },
                new Pose2d(0, 0, Rotation2d.kZero));
    }

    public void addSwerveData(SwerveModulePosition[] swerveModulePositions, Rotation2d gyroAngle, double timestamp) {
        swervePoseEstimator.updateWithTime(timestamp, gyroAngle, swerveModulePositions);
    }

    public Pose2d getEstimatedPose() {
        return swervePoseEstimator.getEstimatedPosition();
    }

    public void resetPose(Pose2d pose, SwerveModulePosition[] wModulePositions, Rotation2d gyroAngle) {
        swervePoseEstimator.resetPosition(gyroAngle, wModulePositions, pose);
    }

    //TODO add vision etc deviations see the seson code for more details
  

    

}
