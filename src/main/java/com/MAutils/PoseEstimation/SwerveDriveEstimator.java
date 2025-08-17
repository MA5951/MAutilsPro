package com.MAutils.PoseEstimation;

import com.MAutils.Swerve.SwerveSystem;
import com.MAutils.Swerve.SwerveSystemConstants;
import com.MAutils.Swerve.Utils.CollisionDetector;
import com.MAutils.Swerve.Utils.SkidDetector;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.wpilibj.Timer;

public class SwerveDriveEstimator {
    private final double MAX_UPDATE_ANGLE = 10.0;
    private final double MIN_SKIP_ANGLE = 4;
    private final double SKIP_ODOMETRY_Gs = 4;

    private SwerveModulePosition[] lastPositions = new SwerveModulePosition[] {
            new SwerveModulePosition(0, new Rotation2d()),
            new SwerveModulePosition(0, new Rotation2d()),
            new SwerveModulePosition(0, new Rotation2d()),
            new SwerveModulePosition(0, new Rotation2d())
    };

    private Rotation2d lastGyroRotation, prevAngle, currAngle;
    private final SwerveSystem swerveSystem;
    private double gyroDelta, deltaDistance, deltaTheta;
    private final SkidDetector skidDetector;
    private final CollisionDetector collisionDetector;
    private boolean[] skipModule = new boolean[4];
    private double[] sampleTimestamps;
    private final PoseEstimatorSource odometrySource;
    private Twist2d loopTwistSum= new Twist2d(), odometryTwist;
    private Translation2d totalDelta = new Translation2d(), arcDelta;

    public SwerveDriveEstimator(SwerveSystemConstants swerveConstants, SwerveSystem swerveSystem) {
        this.swerveSystem = swerveSystem;
        this.lastGyroRotation = Rotation2d.fromDegrees(swerveSystem.getGyroData().yaw);

        this.skidDetector = new SkidDetector(swerveConstants, swerveSystem::getCurrentStates);
        this.collisionDetector = new CollisionDetector(swerveSystem::getGyroData);

        this.odometrySource = new PoseEstimatorSource(
                () -> loopTwistSum, () -> getTranslationFOM(), () -> getRotationFOM(), () -> Timer.getFPGATimestamp());

        PoseEstimator.addSource(odometrySource);

    }

    // Deltas
    private Twist2d getTranslationDelta(SwerveModulePosition[] currentPositions) {
        totalDelta = new Translation2d();

        for (int i = 0; i < currentPositions.length; i++) {
            deltaDistance = currentPositions[i].distanceMeters - lastPositions[i].distanceMeters;
            prevAngle = lastPositions[i].angle;
            currAngle = currentPositions[i].angle;
            deltaTheta = currAngle.minus(prevAngle).getRadians();

            if (Math.abs(deltaTheta) < 1e-5) {
                arcDelta = new Translation2d(deltaDistance, currAngle);
            } else {
                Translation2d v1 = new Translation2d(deltaDistance / deltaTheta,
                        prevAngle.minus(Rotation2d.fromRadians(Math.PI / 2)));
                Translation2d v2 = v1.rotateBy(Rotation2d.fromRadians(deltaTheta));

                arcDelta = v2.minus(v1);
            }

            totalDelta = totalDelta.plus(arcDelta);
        }

        lastPositions = currentPositions;

        return new Twist2d(
                totalDelta.getX() / 4,
                totalDelta.getY() / 4,
                0);
    }

    private double getGyroDelta(Rotation2d currentGyro) {
        gyroDelta = currentGyro.minus(lastGyroRotation).getRadians();
        lastGyroRotation = currentGyro;
        return gyroDelta;
    }

    private void updateOdometryTwist(SwerveModulePosition[] currentPositions, Rotation2d currentGyro) {
        odometryTwist = getTranslationDelta(currentPositions);
        odometryTwist.dtheta = getGyroDelta(currentGyro);
    }

    // FOMs
    private double getTranslationFOM() {
        return 1;// To talk with rader
    }

    private double getRotationFOM() {
        return 1.0;// To talk with rader
    }

    private void skipModulesWithGyro() {
        if (swerveSystem.getGyroData().pitch > MIN_SKIP_ANGLE) {
            skipModule[0] = true;
            skipModule[1] = true;
        } else if (swerveSystem.getGyroData().pitch < -MIN_SKIP_ANGLE) {
            skipModule[2] = true;
            skipModule[3] = true;
        }

        if (swerveSystem.getGyroData().roll > MIN_SKIP_ANGLE) {
            skipModule[0] = true;
            skipModule[2] = true;
        } else if (swerveSystem.getGyroData().roll < -MIN_SKIP_ANGLE) {
            skipModule[1] = true;
            skipModule[3] = true;
        }
    }

    // Update
    public void updateOdometry() {
        skidDetector.calculateSkid();
        collisionDetector.calculateCollision();

        if (!(collisionDetector.getForceVector() > SKIP_ODOMETRY_Gs || swerveSystem.getGyroData().pitch > MAX_UPDATE_ANGLE
                || swerveSystem.getGyroData().roll > MAX_UPDATE_ANGLE)) {
            skipModule = skidDetector.getIsSkidding();

            skipModulesWithGyro();

            loopTwistSum.dx = 0;
            loopTwistSum.dy = 0;
            loopTwistSum.dtheta = 0;

            sampleTimestamps = swerveSystem.getGyroData().odometryYawTimestamps;
            for (int i = 0; i < sampleTimestamps.length; i++) {
                SwerveModulePosition[] wheelPositions = new SwerveModulePosition[4];
                for (int j = 0; j < 4; j++) {
                    wheelPositions[j] = swerveSystem.getSwerveModules()[j].getOdometryPositions()[i];

                    // if (skipModule[j]) {
                    //     lastPositions[j] = wheelPositions[j];

                    // }
                }

                updateOdometryTwist(wheelPositions, swerveSystem.getGyroData().odometryYawPositions[i]);

                loopTwistSum.dx += odometryTwist.dx;
                loopTwistSum.dy += odometryTwist.dy;
                loopTwistSum.dtheta += odometryTwist.dtheta;

            }

            odometrySource.capture();

        }
    }
}
