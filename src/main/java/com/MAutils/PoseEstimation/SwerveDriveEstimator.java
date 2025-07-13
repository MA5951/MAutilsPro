package com.MAutils.PoseEstimation;

import com.MAutils.Swerve.SwerveSystem;
import com.MAutils.Swerve.SwerveSystemConstants;
import com.MAutils.Swerve.Utils.CollisionDetector;
import com.MAutils.Swerve.Utils.SkidDetector;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

public class SwerveDriveEstimator {

    private SwerveModulePosition[] lastPositions;
    private final SwerveModulePosition[] modulesDeltas = new SwerveModulePosition[4];
    private Rotation2d lastGyroRotation;

    private final SwerveDriveKinematics kinematics;
    private final SwerveSystem swerveSystem;

    private Twist2d modulesTwist;
    private double gyroDelta;

    private final SkidDetector skidDetector;
    private final CollisionDetector collisionDetector;

    public SwerveDriveEstimator(SwerveSystemConstants swerveConstants, SwerveSystem swerveSystem) {
        this.kinematics = swerveConstants.kinematics;
        this.swerveSystem = swerveSystem;
        this.lastPositions = swerveSystem.getCurrentPositions();
        this.lastGyroRotation = Rotation2d.fromDegrees(swerveSystem.getGyroData().yaw);

        this.skidDetector = new SkidDetector(swerveConstants, swerveSystem::getCurrentStates);
        this.collisionDetector = new CollisionDetector(swerveSystem::getGyroData);
    }

    public Twist2d getTranslationDelta(SwerveModulePosition[] currentPositions) {
        modulesTwist = kinematics.toTwist2d(lastPositions, currentPositions);
        lastPositions = currentPositions;
        modulesTwist.dtheta = 0;
        return modulesTwist;
    }

    public double getTranslationFOM() {
        return 1.0;
    }

    public double getRotationFOM() {
        return 1.0;
    }

    public double getGyroDelta(Rotation2d currentGyro) {
        gyroDelta = currentGyro.minus(lastGyroRotation).getRadians();
        lastGyroRotation = currentGyro;
        return gyroDelta;
    }

    public void update() {
        skidDetector.calculateSkid();
    }
}
