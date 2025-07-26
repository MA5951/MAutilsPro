package com.MAutils.PoseEstimation;

import com.MAutils.Logger.MALog;
import com.MAutils.Swerve.SwerveSystem;
import com.MAutils.Swerve.SwerveSystemConstants;
import com.MAutils.Swerve.Utils.CollisionDetector;
import com.MAutils.Swerve.Utils.SkidDetector;
import com.MAutils.Utils.Constants;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
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

    private double cumDx = 0; // cumulative dx for translation FOM
    private double cumDy = 0; // cumulative dy for translation FOM
    private double cumDtheta = 0; // cumulative dtheta for rotation FOM

    public SwerveDriveEstimator(SwerveSystemConstants swerveConstants, SwerveSystem swerveSystem) {
        this.kinematics = swerveConstants.kinematics;
        this.swerveSystem = swerveSystem;
        this.lastGyroRotation = Rotation2d.fromDegrees(swerveSystem.getGyroData().yaw);

        this.skidDetector = new SkidDetector(swerveConstants, swerveSystem::getCurrentStates);
        this.collisionDetector = new CollisionDetector(swerveSystem::getGyroData);

        this.poseEstimatorSourceGyro = new PoseEstimatorSource();// () ->
        // getRotationFOM()
        this.poseEstimatorSourceTranslation = new PoseEstimatorSource();// () -> getTranslationFOM()

        PoseEstimator.addSource(poseEstimatorSourceTranslation);
        PoseEstimator.addSource(poseEstimatorSourceGyro);
    }

    // Deltas
    public Twist2d getTranslationDelta(SwerveModulePosition[] currentPositions) {
    Translation2d totalDelta = new Translation2d();
    double totalDeltaTheta = 0;

    for (int i = 0; i < currentPositions.length; i++) {
        double deltaDistance = currentPositions[i].distanceMeters - lastPositions[i].distanceMeters;
        Rotation2d prevAngle = lastPositions[i].angle;
        Rotation2d currAngle = currentPositions[i].angle;
        double deltaTheta = currAngle.minus(prevAngle).getRadians();

        Translation2d arcDelta;

        // Handle small or zero rotation to avoid division by zero
        if (Math.abs(deltaTheta) < 1e-5) {
            arcDelta = new Translation2d(deltaDistance, currAngle);
        } else {
            double radius = deltaDistance / deltaTheta;

            // Vectors from center of arc to previous and current module positions
            Translation2d v1 = new Translation2d(radius, prevAngle.minus(Rotation2d.fromRadians(Math.PI / 2)));
            Translation2d v2 = v1.rotateBy(Rotation2d.fromRadians(deltaTheta));

            arcDelta = v2.minus(v1);
        }

        totalDelta = totalDelta.plus(arcDelta);
        totalDeltaTheta += deltaTheta;
    }

    lastPositions = currentPositions;

    return new Twist2d(
        totalDelta.getX() / 4,
        totalDelta.getY() / 4,
        totalDeltaTheta / 4
    );
}

    public double getGyroDelta(Rotation2d currentGyro) {
        gyroDelta = currentGyro.minus(lastGyroRotation).getRadians();
        lastGyroRotation = currentGyro;
        return gyroDelta;
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

        if (true) {//
            // skipModule = skidDetector.getIsSkidding();

            // if (swerveSystem.getGyroData().pitch > MIN_SKIP_ANGLE) {// TODO cheack
            // skipModule[0] = true;
            // skipModule[1] = true;
            // } else if (swerveSystem.getGyroData().pitch < -MIN_SKIP_ANGLE) {
            // skipModule[2] = true;
            // skipModule[3] = true;
            // }

            // if (swerveSystem.getGyroData().roll > MIN_SKIP_ANGLE) {
            // skipModule[0] = true;
            // skipModule[2] = true;
            // } else if (swerveSystem.getGyroData().roll < -MIN_SKIP_ANGLE) {
            // skipModule[1] = true;
            // skipModule[3] = true;
            // }

            cumDx = 0;
            cumDy = 0;
            cumDtheta = 0;

            double[] sampleTimestamps = swerveSystem.getGyroData().odometryYawTimestamps;
            for (int i = 0; i < sampleTimestamps.length; i++) {
                SwerveModulePosition[] wheelPositions = new SwerveModulePosition[4];
                for (int j = 0; j < 4; j++) {
                    wheelPositions[j] = swerveSystem.getSwerveModules()[j].getOdometryPositions()[i];

                    // if (skipModule[j]) {
                    // lastPositions[j] = wheelPositions[j];

                    // }
                }
                // MALog.log("/Pose Estimator/Swerve Drive Estimator/1DX",
                // getTranslationDelta(wheelPositions).dx);

                Twist2d translationDelta = getTranslationDelta(wheelPositions);
                translationDelta.dtheta = getGyroDelta(swerveSystem.getGyroData().odometryYawPositions[i]); // Ignore
                                                                                                            // rotation
                                                                                                            // for
                                                                                                            // translation
                cumDx += translationDelta.dx;
                cumDy += translationDelta.dy;
                cumDtheta += translationDelta.dtheta;// FOM


            }

            poseEstimatorSourceTranslation.addMeasurement(
                    new Twist2d(cumDx, cumDy, cumDtheta), 1,
                    Timer.getFPGATimestamp());

        }
    }
}
