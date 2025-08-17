
package com.MAutils.Swerve;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.MAutils.Logger.MALog;
import com.MAutils.PoseEstimation.SwerveDriveEstimator;
import com.MAutils.Simulation.SimulationManager;
import com.MAutils.Simulation.Simulatables.SwerveSimulation;
import com.MAutils.Swerve.IOs.PhoenixOdometryThread;
import com.MAutils.Swerve.IOs.Gyro.Gyro;
import com.MAutils.Swerve.IOs.Gyro.GyroIO.GyroData;
import com.MAutils.Swerve.IOs.SwerveModule.SwerveModule;
import com.MAutils.Swerve.IOs.SwerveModule.SwerveModuleIO.SwerveModuleData;
import com.MAutils.Swerve.Utils.DriveFeedforwards;
import com.MAutils.Swerve.Utils.SwerveSetpoint;
import com.MAutils.Swerve.Utils.SwerveSetpointGenerator;
import com.MAutils.Swerve.Utils.SwerveState;
import com.MAutils.Utils.DeafultRobotConstants;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;

public class SwerveSystem extends SubsystemBase {
    private static SwerveSystem instance;

    private SwerveState currentState;
    private SwerveDriveEstimator swerveDriveEstimator;

    public static final Lock odometryLock = new ReentrantLock();
    private final SwerveSystemConstants swerveConstants;
    private final SwerveModule[] swerveModules;// FL FR RL RR
    private SwerveModuleData[] swerveModuleData = new SwerveModuleData[4];
    private final Gyro gyro;
    private ChassisSpeeds currentSpeeds;
    private SwerveSetpoint swerveSetpoint;;
    private final SwerveSetpointGenerator swerveSetpointGenerator;
    private final SwerveModuleState[] currentStates = new SwerveModuleState[4];
    private final SwerveModulePosition[] currentPositions = new SwerveModulePosition[4];

    private SwerveSystem(SwerveSystemConstants swerveConstants) {
        super();
        this.swerveConstants = swerveConstants;

        swerveModules = swerveConstants.getModules();
        gyro = swerveConstants.getGyro();

        swerveSetpointGenerator = new SwerveSetpointGenerator(swerveConstants.getRobotConfig(),
                swerveConstants.MAX_STEER_VELOCITY_RADS);

        if (!Robot.isReal()) {
            SimulationManager.registerSimulatable(new SwerveSimulation(swerveConstants));
        }

        swerveDriveEstimator = new SwerveDriveEstimator(swerveConstants, this);

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

        for (int i = 0; i < swerveModules.length; i++) {
            currentPositions[i] = swerveModules[i].getPosition();
        }

        swerveSetpoint = new SwerveSetpoint(new ChassisSpeeds(0, 0, 0), currentStates, DriveFeedforwards.zeros(4));
    }

    public SwerveSystemConstants getSwerveConstants() {
        return swerveConstants;
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
            swerveModuleData[i] = swerveModules[i].getModuleData();
            currentStates[i] = swerveModules[i].getState();
        }

        for (int i = 0; i < swerveModules.length; i++) {
            currentPositions[i] = swerveModules[i].getPosition();
        }

        currentSpeeds = swerveConstants.kinematics.toChassisSpeeds(currentStates);

        swerveDriveEstimator.updateOdometry();
        logSwerve();

    }

    // Public Methods
    public SwerveModuleState[] getCurrentStates() {
        return currentStates;
    }

    public SwerveModulePosition[] getCurrentPositions() {
        return currentPositions;
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

    public void runSwerveStates(SwerveModuleState[] states) {
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

    private void logSwerve() {
        MALog.log("/Subsystems/Swerve/Chassis Speeds/Current", currentSpeeds);
        MALog.logSwerveModuleStates("/Subsystems/Swerve/States/Current", currentStates);
    }

    public static SwerveSystem getInstance(SwerveSystemConstants swerveConstants) {
        if (instance == null) {
            instance = new SwerveSystem(swerveConstants);
        }
        return instance;
    }

}
