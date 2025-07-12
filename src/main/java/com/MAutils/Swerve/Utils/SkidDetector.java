
package com.MAutils.Swerve.Utils;

import java.util.function.Supplier;

import com.MAutils.Logger.MALog;
import com.MAutils.Swerve.SwerveSystemConstants;
import com.MAutils.Utils.VectorUtil;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public class SkidDetector {

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
    private double skiddingRatio = 0;

    public SkidDetector(SwerveSystemConstants constants, Supplier<SwerveModuleState[]> statesSupplier) {
        this.kinematics = constants.kinematics;
        this.statesSupplier = statesSupplier;
    }

    public double getSkiddingRatio() {
        minimumTranslationalSpeed = Integer.MAX_VALUE;
        maximumTranslationalSpeed = -Integer.MAX_VALUE;
        measurSpeeds = kinematics.toChassisSpeeds(statesSupplier.get());
        swerveStatesRotationalPart = kinematics
                .toSwerveModuleStates(new ChassisSpeeds(0, 0, measurSpeeds.omegaRadiansPerSecond));

        for (int i = 0; i < statesSupplier.get().length; i++) {
            swerveStateMeasuredAsVector = VectorUtil.getVectorFromSwerveState(statesSupplier.get()[i]);
            swerveStatesRotationalPartAsVector = VectorUtil.getVectorFromSwerveState(swerveStatesRotationalPart[i]);
            swerveStatesTranslationalPartAsVector = swerveStateMeasuredAsVector
                    .minus(swerveStatesRotationalPartAsVector);
            swerveStatesTranslationalPartMagnitudes[i] = swerveStatesTranslationalPartAsVector.getNorm();
        }

        for (double translationalSpeed : swerveStatesTranslationalPartMagnitudes) {
            maximumTranslationalSpeed = Math.max(maximumTranslationalSpeed, translationalSpeed);
            minimumTranslationalSpeed = Math.min(minimumTranslationalSpeed, translationalSpeed);
        }
        MALog.log("/Subsystems/Swerve/Skid/Max Speed", maximumTranslationalSpeed);
        MALog.log("/Subsystems/Swerve/Skid/Min Speed", minimumTranslationalSpeed);


        skiddingRatio = maximumTranslationalSpeed / minimumTranslationalSpeed;

        MALog.log("/Subsystems/Swerve/Skid/Skidding Ratio", skiddingRatio);
        return skiddingRatio;
    }

}
