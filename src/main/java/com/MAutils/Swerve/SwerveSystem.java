
package com.MAutils.Swerve;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.MAutils.Logger.MALog;
import com.MAutils.PoseEstimation.DeafultPoseEstimator;
import com.MAutils.Swerve.IOs.PhoenixOdometryThread;
import com.MAutils.Swerve.IOs.Gyro.Gyro;
import com.MAutils.Swerve.IOs.Gyro.GyroIO.GyroData;
import com.MAutils.Swerve.IOs.SwerveModule.SwerveModule;
import com.MAutils.Swerve.IOs.SwerveModule.SwerveModuleIO.SwerveModuleData;
import com.MAutils.Swerve.Utils.DriveFeedforwards;
import com.MAutils.Swerve.Utils.SkidAndCollisionDetector;
import com.MAutils.Swerve.Utils.SwerveSetpoint;
import com.MAutils.Swerve.Utils.SwerveSetpointGenerator;
import com.MAutils.Swerve.Utils.SwerveState;
import com.MAutils.Utils.DeafultRobotConstants;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveSystem extends SubsystemBase {
    private SwerveState currentState;


    public static final Lock odometryLock = new ReentrantLock();
    private final SwerveSystemConstants swerveConstants;
    private final SwerveModule[] swerveModules;// FL FR RL RR
    private SwerveModuleData[] swerveModuleData = new SwerveModuleData[4];
    private final Gyro gyro;
    private ChassisSpeeds currentSpeeds;
    private SwerveSetpoint swerveSetpoint;;
    private final SwerveSetpointGenerator swerveSetpointGenerator;
    private final SwerveModuleState[] currentStates = new SwerveModuleState[4];
    private final DeafultPoseEstimator poseEstimator;//TODO: cahge to "PoseEstimator" 
    private final SkidAndCollisionDetector skidAndCollisionDetector;

    public SwerveSystem(SwerveSystemConstants swerveConstants, DeafultPoseEstimator poseEstimator) {
        super();
        this.swerveConstants = swerveConstants;
        this.poseEstimator = poseEstimator;

        swerveModules = swerveConstants.getModules();
        gyro = swerveConstants.getGyro();

        swerveSetpointGenerator = new SwerveSetpointGenerator(swerveConstants.getRobotConfig(),
                Units.rotationsToRadians(10.0));

        skidAndCollisionDetector = new SkidAndCollisionDetector(swerveConstants, gyro::getGyroData,
                this::getCurrentStates);

        PhoenixOdometryThread.getInstance(swerveConstants).start();

        odometryLock.lock();
        gyro.update();
        for (var module : swerveModules) {
            module.update();
        }
        odometryLock.unlock();

        for (int i = 0; i < swerveModules.length; i++) {
            currentStates[i] = swerveModules[i].getState();
        }

        swerveSetpoint = new SwerveSetpoint(new ChassisSpeeds(0, 0, 0), currentStates, DriveFeedforwards.zeros(4));
    }

     public void setState(SwerveState state) {
        this.currentState = state;
        
    }

    public SwerveState getState() {
        return currentState;
    }

    public void periodic() {
        super.periodic();

        odometryLock.lock();
        gyro.update();
        for (var module : swerveModules) {
            module.update();
        }
        odometryLock.unlock();

        for (int i = 0; i < swerveModules.length; i++) {
            currentStates[i] = swerveModules[i].getState();
            swerveModuleData[i] = swerveModules[i].getModuleData();
        }

        currentSpeeds = swerveConstants.kinematics.toChassisSpeeds(currentStates);

        skidAndCollisionDetector.update();
        
        proccedsOdometery();

        logSwerve();

    }

    // Public Methods
    public SwerveModuleState[] getCurrentStates() {
        return currentStates;
    }

    public ChassisSpeeds getChassisSpeeds() {
        return currentSpeeds;
    }

    public void drive(ChassisSpeeds speeds) {
        swerveSetpoint = swerveSetpointGenerator.generateSetpoint(
                swerveSetpoint,
                speeds,
                DeafultRobotConstants.kD);

        MALog.logSwerveModuleStates("/Subsystems/Swerve/States/SetPoint", swerveSetpoint.moduleStates());
        runSwerveStates(swerveSetpoint.moduleStates());
    }

    private void runSwerveStates(SwerveModuleState[] states) {
        for (int i = 0; i < swerveModules.length; i++) {
            swerveModules[i].setSetPoint(states[i]);
        }

    }

    public GyroData getGyroData() {
        return gyro.getGyroData();
    }

    public SwerveModuleData[] getSwerveModuleData() {
        return swerveModuleData;
    }

    public Gyro getGyro() {
        return gyro;
    }

    public SwerveModule[] getSwerveModules() {
        return swerveModules;
    }

    // Private Methods
    private void proccedsOdometery() {
        double[] sampleTimestamps = gyro.getGyroData().odometryYawTimestamps; // All signals are sampled together
        int sampleCount = sampleTimestamps.length;
        for (int i = 0; i < sampleCount; i++) {
            SwerveModulePosition[] wheelPositions = new SwerveModulePosition[4];
            for (int j = 0; j < 4; j++) {
                wheelPositions[j] = swerveModules[j].getOdometryPositions()[i];
            }
            poseEstimator.addSwerveData(wheelPositions, gyro.getGyroData().odometryYawPositions[i],
                    sampleTimestamps[i]);
        }
    }

    private void logSwerve() {
        MALog.log("/Subsystems/Swerve/Chassis Speeds/Current", currentSpeeds);
        MALog.logSwerveModuleStates("/Subsystems/Swerve/States/Current", currentStates);
    }

}
