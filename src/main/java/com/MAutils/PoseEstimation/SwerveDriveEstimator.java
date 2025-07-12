package com.MAutils.PoseEstimation;

import com.MAutils.Swerve.SwerveSystem;
import com.MAutils.Swerve.SwerveSystemConstants;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

public class SwerveDriveEstimator {

    private SwerveModulePosition[] lastPositions;
    private final SwerveModulePosition[] deltas = new SwerveModulePosition[4];
    private Rotation2d lastGyroRotation;

    private final SwerveDriveKinematics kinematics;
    private final SwerveSystem swerveSystem;

    public SwerveDriveEstimator(SwerveSystemConstants swerveConstants, SwerveSystem swerveSystem) {
        this.kinematics = swerveConstants.kinematics;
        this.swerveSystem = swerveSystem;
        this.lastPositions = swerveSystem.getCurrentPositions();
        this.lastGyroRotation = Rotation2d.fromDegrees(swerveSystem.getGyroData().yaw);
    }

    public Twist2d getTranslationDelta(SwerveModulePosition[] currentPositions) {
        for (int i = 0; i < currentPositions.length; i++) {
            double deltaDistance = currentPositions[i].distanceMeters - lastPositions[i].distanceMeters;
            Rotation2d movementDirection = lastPositions[i].angle;
            deltas[i] = new SwerveModulePosition(deltaDistance, movementDirection);
        }

        Twist2d raw = kinematics.toTwist2d(lastPositions, currentPositions);
        lastPositions = currentPositions;
        return new Twist2d(raw.dx, raw.dy, 0.0); // strip rotation
    }

    public double getTranslationFOM() {
        return 1.0; // TODO: implement with SkidDetector
    }

    public double getRotationFOM() {
        return 1.0; // TODO: implement based on gyro trust
    }

    public double getGyroDelta(Rotation2d currentGyro) {
        double dtheta = currentGyro.minus(lastGyroRotation).getRadians();
        lastGyroRotation = currentGyro;
        return dtheta;
    }
}
