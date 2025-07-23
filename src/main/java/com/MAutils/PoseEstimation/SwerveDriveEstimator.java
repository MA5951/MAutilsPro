package com.MAutils.PoseEstimation;


import com.MAutils.Logger.MALog;
import com.MAutils.Swerve.SwerveSystem;
import com.MAutils.Swerve.SwerveSystemConstants;
import com.MAutils.Swerve.Utils.CollisionDetector;
import com.MAutils.Swerve.Utils.SkidDetector;
import com.MAutils.Utils.Constants;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.wpilibj.Timer;

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
    private boolean colliding = false;
    private double translationFOM;
    private boolean[] skipModule = new boolean[4];

    private PoseEstimatorSource poseEstimatorSourceGyro;
    private PoseEstimatorSource poseEstimatorSourceTranslation;

    public SwerveDriveEstimator(SwerveSystemConstants swerveConstants, SwerveSystem swerveSystem) {
        this.kinematics = swerveConstants.kinematics;
        this.swerveSystem = swerveSystem;
        this.lastGyroRotation = Rotation2d.fromDegrees(swerveSystem.getGyroData().yaw);

        this.skidDetector = new SkidDetector(swerveConstants, swerveSystem::getCurrentStates);
        this.collisionDetector = new CollisionDetector(swerveSystem::getGyroData);

        this.poseEstimatorSourceGyro = new PoseEstimatorSource(() -> 1d);//() -> getRotationFOM()
        this.poseEstimatorSourceTranslation = new PoseEstimatorSource(() -> Constants.EPSILON);//() -> getTranslationFOM()
    }

    // Deltas
    public Twist2d getTranslationDelta(SwerveModulePosition[] currentPositions) {
        modulesTwist = kinematics.toTwist2d(lastPositions, currentPositions);
        lastPositions = currentPositions;
        modulesTwist.dtheta = 0;
        return modulesTwist;
    }

    public Twist2d getGyroDelta(Rotation2d currentGyro) {
        gyroDelta = currentGyro.minus(lastGyroRotation).getRadians();
        lastGyroRotation = currentGyro;
        return new Twist2d(0, 0, gyroDelta);
    }

    // FOMs
    public double getTranslationFOM() {
        // Collision
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

        translationFOM = 1;
        MALog.log("/Pose Estimator/Translation FOM", translationFOM);

        if (true) {//
            // skipModule = skidDetector.getIsSkidding();

            // if (swerveSystem.getGyroData().pitch > MIN_SKIP_ANGLE) {// TODO cheack
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
            //}

            double[] sampleTimestamps = swerveSystem.getGyroData().odometryYawTimestamps;
            for (int i = 0; i < sampleTimestamps.length; i++) {
                SwerveModulePosition[] wheelPositions = new SwerveModulePosition[4];
                for (int j = 0; j < 4; j++) {
                    wheelPositions[j] = swerveSystem.getSwerveModules()[j].getOdometryPositions()[i];

                    // if (skipModule[j]) {
                    //     lastPositions[j] = wheelPositions[j];

                    // }
                }

                poseEstimatorSourceTranslation.sendDataWiteTimestemp(
                        getTranslationDelta(wheelPositions),
                        //new Twist2d(0.111, 0, 0),
                        sampleTimestamps[i]
                        //Timer.getFPGATimestamp()
                        );

                // poseEstimatorSourceGyro.sendDataWiteTimestemp(
                //         getGyroDelta(swerveSystem.getGyroData().odometryYawPositions[i]),
                //         sampleTimestamps[i]);


            }

        }
    }
}
