
package com.MAutils.PoseEstimation;

import com.MAutils.Logger.MALog;
import com.MAutils.Swerve.SwerveSystem;
import com.MAutils.Swerve.SwerveSystemConstants;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;

public class PoseEstimatorOld {
    private static PoseEstimatorOld instance;

    protected final SwerveDrivePoseEstimator swervePoseEstimator;
    private final SwerveSystem swerveSystem;
    private Pose2d lookAhedPose = new Pose2d();
    private ChassisSpeeds fieldRelativSpeeds = new ChassisSpeeds();

    private PoseEstimatorOld(SwerveSystemConstants constants, SwerveSystem swerveSystem) {
        swervePoseEstimator = new SwerveDrivePoseEstimator(constants.kinematics, Rotation2d.fromDegrees(0),
                new SwerveModulePosition[] { new SwerveModulePosition(0, Rotation2d.kZero),
                        new SwerveModulePosition(0, Rotation2d.kZero), new SwerveModulePosition(0, Rotation2d.kZero),
                        new SwerveModulePosition(0, Rotation2d.kZero) },
                new Pose2d(0, 0, Rotation2d.kZero));

        this.swerveSystem = swerveSystem;
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

    public void resetPose(Pose2d pose) {
        swervePoseEstimator.resetPosition(Rotation2d.fromDegrees(swerveSystem.getGyroData().yaw),
                swerveSystem.getCurrentPositions(), pose);
    }

    public void addVisionMeasurement(Pose2d visionPose, double timestamp, Matrix<N3, N1> visionMeasurementStdDevs) {
        MALog.log("/Subsystems/Pose Estimator/Last Vision Log", timestamp);
        swervePoseEstimator.setVisionMeasurementStdDevs(visionMeasurementStdDevs);
        swervePoseEstimator.addVisionMeasurement(visionPose, timestamp);
    }

    public Pose2d getLookAhedPose(double time) {
        fieldRelativSpeeds = ChassisSpeeds.fromRobotRelativeSpeeds(swerveSystem.getChassisSpeeds(),
                Rotation2d.fromDegrees(swerveSystem.getGyroData().yaw));
        lookAhedPose = getEstimatedPose().plus(new Transform2d(
                fieldRelativSpeeds.vxMetersPerSecond * time, fieldRelativSpeeds.vyMetersPerSecond * time,
                Rotation2d.fromRadians(fieldRelativSpeeds.omegaRadiansPerSecond * time)));
        return lookAhedPose;
    }// TODO filters?

    public PoseEstimatorOld getInstance(SwerveSystemConstants constants, SwerveSystem swerveSystem) {
        if (instance == null) {
            instance = new PoseEstimatorOld(constants, swerveSystem);
        }
        return instance;
    }

}
