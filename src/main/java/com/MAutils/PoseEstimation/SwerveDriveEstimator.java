package com.MAutils.PoseEstimation;

import com.MAutils.Logger.MALog;
import com.MAutils.Swerve.SwerveSystem;
import com.MAutils.Swerve.SwerveSystemConstants;
import com.MAutils.Swerve.Utils.CollisionDetector;
import com.MAutils.Swerve.Utils.SkidDetector;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

public class SwerveDriveEstimator {
    private final double MAX_UPDATE_ANGLE = 10.0;
    private final double MIN_SKIP_ANGLE = 4;

    private SwerveModulePosition[] lastPositions = new SwerveModulePosition[] {
        new SwerveModulePosition(0, new Rotation2d()),
        new SwerveModulePosition(0, new Rotation2d()),
        new SwerveModulePosition(0, new Rotation2d()),
        new SwerveModulePosition(0, new Rotation2d())
};;
    private Rotation2d lastGyroRotation;

    private final SwerveDriveKinematics kinematics;
    private final SwerveSystem swerveSystem;

    private Twist2d modulesTwist;
    private double gyroDelta;

    private final SkidDetector skidDetector;
    private final CollisionDetector collisionDetector;
    private final FOMPoseEstimator fomPoseEstimator;
    private boolean colliding = false;
    private double translationFOM;
    private boolean[] skipModule = new boolean[4];
    private SwerveModulePosition[] wheelPositions = new SwerveModulePosition[] {
            new SwerveModulePosition(0, new Rotation2d()),
            new SwerveModulePosition(0, new Rotation2d()),
            new SwerveModulePosition(0, new Rotation2d()),
            new SwerveModulePosition(0, new Rotation2d())
    };

    public SwerveDriveEstimator(SwerveSystemConstants swerveConstants, SwerveSystem swerveSystem,
            FOMPoseEstimator fomPoseEstimator) {
        this.fomPoseEstimator = fomPoseEstimator;
        this.kinematics = swerveConstants.kinematics;
        this.swerveSystem = swerveSystem;
        this.lastGyroRotation = Rotation2d.fromDegrees(swerveSystem.getGyroData().yaw);

        this.skidDetector = new SkidDetector(swerveConstants, swerveSystem::getCurrentStates);
        this.collisionDetector = new CollisionDetector(swerveSystem::getGyroData);
    }

    // Deltas
    public Twist2d getTranslationDelta(SwerveModulePosition[] currentPositions) {
        modulesTwist = kinematics.toTwist2d(lastPositions, currentPositions);
        lastPositions = currentPositions;
        modulesTwist.dtheta = 0;
        return modulesTwist;
    }

    public double getGyroDelta(Rotation2d currentGyro) {
        gyroDelta = currentGyro.minus(lastGyroRotation).getRadians();
        lastGyroRotation = currentGyro;
        return gyroDelta;
    }

    // FOMs
    public double getTranslationFOM() {
        //Collision
        if (collisionDetector.getIsColliding()) {
            colliding = true;
        } else if (colliding && skidDetector.getNumOfSkiddingModules() == 0) {
            colliding = false;
        }

        if (colliding || skidDetector.getNumOfSkiddingModules() > 2
                || swerveSystem.getGyroData().pitch > MAX_UPDATE_ANGLE
                || swerveSystem.getGyroData().roll > MAX_UPDATE_ANGLE) {
            return 0.0;
        }

        return 1.0 - (skidDetector.getNumOfSkiddingModules() * 0.25);
    }

    public double getRotationFOM() {
        return 1.0;
    }

    // Update
    public void updateOdometry() {
        skidDetector.calculateSkid();
        collisionDetector.calculateCollision();

        translationFOM = getTranslationFOM();
        MALog.log("/Pose Estimator/Translation FOM", translationFOM);

        if (translationFOM != 0) {
            skipModule = skidDetector.getIsSkidding();

            // if (swerveSystem.getGyroData().pitch > MIN_SKIP_ANGLE) {// TODO cheack directions
            //     skipModule[0] = true;
            //     skipModule[1] = true;
            // } else if (swerveSystem.getGyroData().pitch < -MIN_SKIP_ANGLE) {
            //     skipModule[2] = true;
            //     skipModule[3] = true;
            // }

            // if (swerveSystem.getGyroData().roll > MIN_SKIP_ANGLE) {
            //     skipModule[0] = true;
            //     skipModule[2] = true;
            // } else if (swerveSystem.getGyroData().roll < -MIN_SKIP_ANGLE) {
            //     skipModule[1] = true;
            //     skipModule[3] = true;
            // }

            double[] sampleTimestamps = swerveSystem.getGyroData().odometryYawTimestamps;
            int sampleCount = sampleTimestamps.length;
            for (int i = 0; i < sampleCount; i++) {
                for (int j = 0; j < 4; j++) {
                    // if (skipModule[j]) {
                    //     wheelPositions[j].distanceMeters = 0;
                    // } else {
                        
                    // }

                    wheelPositions[j] = swerveSystem.getSwerveModules()[j].getOdometryPositions()[i];
                }

                

                fomPoseEstimator.addTranslationDelta(
                        getTranslationDelta(wheelPositions),
                        translationFOM);

                fomPoseEstimator.addRotationDelta(
                        getGyroDelta(swerveSystem.getGyroData().odometryYawPositions[i]),
                        getRotationFOM());

            }
        }

    }
}
