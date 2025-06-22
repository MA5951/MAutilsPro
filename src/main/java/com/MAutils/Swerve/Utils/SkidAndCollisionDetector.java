
package com.MAutils.Swerve.Utils;

import java.util.function.Supplier;

import com.MAutils.Logger.MALog;
import com.MAutils.Swerve.SwerveSystemConstants;
import com.MAutils.Swerve.IOs.Gyro.GyroIO.GyroData;
import com.MAutils.Utils.VectorUtil;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public class SkidAndCollisionDetector {

    private final Supplier<GyroData> gyroDataSupplier;

    private final SwerveDriveKinematics kinematics;
    private final Supplier<SwerveModuleState[]> statesSupplier;
    private ChassisSpeeds measurSpeeds;
    private SwerveModuleState[] swerveStatesRotationalPart;
    private double[] swerveStatesTranslationalPartMagnitudes = new double[4];
    private Translation2d swerveStateMeasuredAsVector;
    private Translation2d swerveStatesRotationalPartAsVector;
    private Translation2d swerveStatesTranslationalPartAsVector;
    private double maximumTranslationalSpeed = 0;
    private double minimumTranslationalSpeed = 0;


    public SkidAndCollisionDetector(SwerveSystemConstants constants,Supplier<GyroData> gyroDataSupplier, Supplier<SwerveModuleState[]> statesSupplier) {
        this.gyroDataSupplier = gyroDataSupplier;
        this.kinematics = constants.kinematics;
        this.statesSupplier = statesSupplier;
    }

    public double getForceVectorSize() {
        return Math.sqrt(Math.pow(getCollisionVector().getX(), 2) +
        Math.pow(getCollisionVector().getY(), 2));
    }

    public Translation2d getCollisionVector() {
        return new Translation2d(gyroDataSupplier.get().accelX, gyroDataSupplier.get().accelY);
    }


    public double getSkiddingRatio() {
        measurSpeeds = kinematics.toChassisSpeeds(statesSupplier.get());
        swerveStatesRotationalPart = kinematics.toSwerveModuleStates(new ChassisSpeeds(0, 0, measurSpeeds.omegaRadiansPerSecond));
        
        for (int i = 0; i < statesSupplier.get().length; i++) {
            swerveStateMeasuredAsVector = VectorUtil.getVectorFromSwerveState(statesSupplier.get()[i]);
            swerveStatesRotationalPartAsVector = VectorUtil.getVectorFromSwerveState(swerveStatesRotationalPart[i]);
            swerveStatesTranslationalPartAsVector = swerveStateMeasuredAsVector.minus(swerveStatesRotationalPartAsVector);
            swerveStatesTranslationalPartMagnitudes[i] = swerveStatesTranslationalPartAsVector.getNorm();
        }

        
        for (double translationalSpeed : swerveStatesTranslationalPartMagnitudes) {
            maximumTranslationalSpeed = Math.max(maximumTranslationalSpeed, translationalSpeed);
            minimumTranslationalSpeed = Math.min(minimumTranslationalSpeed, translationalSpeed);
        }

        return maximumTranslationalSpeed / minimumTranslationalSpeed;
    }

    public void update() {
        MALog.log("/Subsystems/Swerve/Collision Skid/Collision Force", getForceVectorSize());
        MALog.log("/Subsystems/Swerve/Collision Skid/Skidding Ratio", getSkiddingRatio());
    }

}
