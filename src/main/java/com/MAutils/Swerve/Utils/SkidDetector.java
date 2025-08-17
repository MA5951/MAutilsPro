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
    private static final double SKID_THRESHOLD = 1.1;

    private final SwerveDriveKinematics kinematics;
    private final Supplier<SwerveModuleState[]> statesSupplier;
    private ChassisSpeeds measurSpeeds;
    private SwerveModuleState[] swerveStatesRotationalPart;
    private double[] swerveStatesTranslationalPartMagnitudes = new double[4];
    private boolean[] isSkidding = new boolean[4];
    private double[] skidRatio = new double[4];

    private Translation2d swerveStateMeasuredAsVector;
    private Translation2d swerveStatesRotationalPartAsVector;
    private Translation2d swerveStatesTranslationalPartAsVector;
    private double minimumTranslationalSpeed = 0;
    private int numOfSkiddingModules = 0;

    public SkidDetector(SwerveSystemConstants constants, Supplier<SwerveModuleState[]> statesSupplier) {
        this.kinematics = constants.kinematics;
        this.statesSupplier = statesSupplier;
    }

    public void calculateSkid() {
        minimumTranslationalSpeed = Double.MAX_VALUE;
        measurSpeeds = kinematics.toChassisSpeeds(statesSupplier.get());
        swerveStatesRotationalPart = kinematics
                .toSwerveModuleStates(new ChassisSpeeds(0, 0, measurSpeeds.omegaRadiansPerSecond));

        // Calculate translational magnitudes and find minimum in one loop
        for (int i = 0; i < 4; i++) {
            swerveStateMeasuredAsVector = VectorUtil.getVectorFromSwerveState(statesSupplier.get()[i]);
            swerveStatesRotationalPartAsVector = VectorUtil.getVectorFromSwerveState(swerveStatesRotationalPart[i]);
            swerveStatesTranslationalPartAsVector = swerveStateMeasuredAsVector
                    .minus(swerveStatesRotationalPartAsVector);
            swerveStatesTranslationalPartMagnitudes[i] = swerveStatesTranslationalPartAsVector.getNorm();
            
            minimumTranslationalSpeed = Math.min(minimumTranslationalSpeed, swerveStatesTranslationalPartMagnitudes[i]);
        }

        if (minimumTranslationalSpeed > 0) {//TODO epsilon after merge
            MALog.log("/Pose Estimator/Skid/Minimum Translational Speed",
                    minimumTranslationalSpeed);
            
            numOfSkiddingModules = 0;
            
            // Calculate skid ratios and detect skidding in one loop
            for (int i = 0; i < 4; i++) {
                skidRatio[i] = swerveStatesTranslationalPartMagnitudes[i] / minimumTranslationalSpeed;
                isSkidding[i] = skidRatio[i] > SKID_THRESHOLD;
                
                if (isSkidding[i]) {
                    numOfSkiddingModules++;
                }
                
                MALog.log("/Pose Estimator/Skid/Is Skidding/" + i, isSkidding[i]);
                MALog.log("/Pose Estimator/Skid/Skid Ratios/" + i, skidRatio[i]);
            }
        }
    }

    public boolean[] getIsSkidding() {
        return isSkidding;
    }

    public int getNumOfSkiddingModules() {
        return numOfSkiddingModules;
    }

}
