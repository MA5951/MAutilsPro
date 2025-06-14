
package com.MAutils.Swerve;

import org.ironmaple.simulation.drivesims.COTS;
import org.ironmaple.simulation.drivesims.SwerveDriveSimulation;
import org.ironmaple.simulation.drivesims.configs.DriveTrainSimulationConfig;

import com.MAutils.Swerve.IOs.Gyro.Gyro;
import com.MAutils.Swerve.IOs.Gyro.GyroPiegon;
import com.MAutils.Swerve.IOs.Gyro.GyroSim;
import com.MAutils.Swerve.IOs.SwerveModule.SwerveModule;
import com.MAutils.Swerve.IOs.SwerveModule.SwerveModuleSim;
import com.MAutils.Swerve.IOs.SwerveModule.SwerveModuleTalonFX;
import com.MAutils.Swerve.Utils.ModuleLimits;
import com.MAutils.Swerve.Utils.PPHolonomicDriveController;
import com.MAutils.Swerve.Utils.SwerveModuleID;
import com.MAutils.Utils.CANBusID;
import com.pathplanner.lib.config.PIDConstants;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.Robot;

public class SwerveConstants {

    public enum GearRatio {
        L1(8.14, 1),
        L2(6.75, 2),
        L3(6.12, 3),
        L1Pluse(7.13, 7),
        L2Pluse(5.9, 8),
        L3Pluse(5.36, 9);

        public final double gearRatio;
        public final int ratioNumber;

        private GearRatio(double ratio, int ratioNumber) {
            gearRatio = ratio;
            this.ratioNumber = ratioNumber;
        }
    }

    public enum WheelType {
        BLACK_TREAD(1.426, 1.5),
        WCP_TREAD(1.0, 1.5); // TODO find

        public final double coFriction;
        public final double widthMM;

        private WheelType(double coFriction, double widthInches) {
            this.coFriction = coFriction;
            this.widthMM = Units.inchesToMeters(widthInches) * 1000; // Convert inches to mm
        }
    }

    public boolean OPTIMIZE = true;
    public double WIDTH = 0.566;
    public double LENGTH = 0.566;
    public final double RADIUS = Math.sqrt(
            Math.pow(WIDTH, 2) + Math.pow(LENGTH, 2)) / 2.0;
    public final double BUMPER_WIDTH = WIDTH + 0.16; // TODO cheack
    public final double BUMPER_LENGTH = LENGTH + 0.16; // TODO cheack
    public double ROBOT_MASS = 62;
    public double TURNING_GEAR_RATIO = 150d / 7;
    public GearRatio DRIVE_GEAR_RATIO = GearRatio.L2;
    public double WHEEL_RADIUS = 0.0508;
    public final double WHEEL_CIRCUMFERENCE = 2 * WHEEL_RADIUS * Math.PI;
    public DCMotor DRIVE_MOTOR = DCMotor.getKrakenX60(1);
    public DCMotor TURNING_MOTOR = DCMotor.getFalcon500(1);
    public WheelType WHEEL_TYPE = WheelType.BLACK_TREAD;

    // front left module
    public SwerveModuleID FRONT_LEFT_MODULE_ID;

    // front right module
    public SwerveModuleID FRONT_RIGHT_MODULE_ID;

    // rear left module
    public SwerveModuleID REAR_LEFT_MODULE_ID;

    // rear right module
    public SwerveModuleID REAR_RIGHT_MODULE_ID;

    public final SwerveModuleID[] MODULES_ID_ARRY = new SwerveModuleID[] {
            FRONT_LEFT_MODULE_ID, FRONT_RIGHT_MODULE_ID,
            REAR_LEFT_MODULE_ID, REAR_RIGHT_MODULE_ID };

    // Piegon
    public CANBusID PIEGEON_CAN_ID;

    // Module locations
    public final Translation2d frontLeftLocation = new Translation2d(
            -WIDTH / 2,
            LENGTH / 2);

    public final Translation2d frontRightLocation = new Translation2d(
            WIDTH / 2,
            LENGTH / 2);

    public final Translation2d rearLeftLocation = new Translation2d(
            -WIDTH / 2,
            -LENGTH / 2);

    public final Translation2d rearRightLocation = new Translation2d(
            WIDTH / 2,
            -LENGTH / 2);

    public final Translation2d[] modulesLocationArry = new Translation2d[] { frontLeftLocation,
            frontRightLocation, rearLeftLocation, rearRightLocation };

    public PPHolonomicDriveController realPPController = new PPHolonomicDriveController(new PIDConstants(0),
            new PIDConstants(0));
    public PPHolonomicDriveController simPPController = new PPHolonomicDriveController(new PIDConstants(0),
            new PIDConstants(0));;

    public final PPHolonomicDriveController getPPController() {
        return RobotBase.isReal() ? realPPController : simPPController;
    }

    // Modules config
    public static int TELEOP_SLOT_CONFIG = 0;
    public static int AUTO_SLOT_CONFIG = 1;

    public double TURNING_kP = 93;
    public double TURNING_kI = 0;
    public double TURNING_kD = 0;
    public double TURNING_kS = 0;
    public double TURNING_kV = 0;
    public double TURNING_kA = 0;

    public double AUTO_DRIVE_kP = 0;
    public double AUTO_DRIVE_kI = 0;
    public double AUTO_DRIVE_kD = 0;
    public double AUTO_DRIVE_kS = 0;
    public double AUTO_DRIVE_kV = 0.857;
    public double AUTO_DRIVE_kA = 0;

    public double TELEOP_DRIVE_kP = 0;
    public double TELEOP_DRIVE_kI = 0;
    public double TELEOP_DRIVE_kD = 0;
    public double TELEOP_DRIVE_kS = 0;
    public double TELEOP_DRIVE_kV = 0.857;
    public double TELEOP_DRIVE_kA = 0;

    public double TURNING__CURRENT_LIMIT = 35;
    public boolean TURNING_ENABLE_CURRENT_LIMIT = true;

    public double DRIVE__SLIP_LIMIT = 35;
    public boolean DRIVE_ENABLE_CURRENT_LIMIT = true;

    // Kinamtics
    public double MAX_VELOCITY = 4.1;
    public double MAX_ACCELERATION = 5;
    public double MAX_ANGULAR_VELOCITY = MAX_VELOCITY / RADIUS;// Radians
    public final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(
            frontLeftLocation, frontRightLocation,
            rearLeftLocation, rearRightLocation);

    public final DriveTrainSimulationConfig DRIVE_TRAIN_SIMULATION_CONFIG = DriveTrainSimulationConfig
            .Default()

            .withGyro(COTS.ofPigeon2())
            .withRobotMass(edu.wpi.first.units.Units.Kilogram.of(ROBOT_MASS))
            .withSwerveModule(COTS.ofMark4i(
                    DRIVE_MOTOR,
                    TURNING_MOTOR,
                    WHEEL_TYPE.coFriction,
                    DRIVE_GEAR_RATIO.ratioNumber))
            .withTrackLengthTrackWidth(edu.wpi.first.units.Units.Meter.of(LENGTH),
                    edu.wpi.first.units.Units.Meter.of(WIDTH))
            .withBumperSize(edu.wpi.first.units.Units.Meter.of(BUMPER_WIDTH),
                    edu.wpi.first.units.Units.Meter.of(BUMPER_LENGTH));

    public SwerveDriveSimulation SWERVE_DRIVE_SIMULATION = new SwerveDriveSimulation(
            DRIVE_TRAIN_SIMULATION_CONFIG, new Pose2d(0, 0, new Rotation2d()));

    // Module Limits
    public final ModuleLimits DEFUALT = new ModuleLimits(MAX_VELOCITY, MAX_ACCELERATION,
            Units.degreesToRadians(700));

    public double ODOMETRY_UPDATE_RATE = 250;

    public SwerveConstants() {

    }

    public SwerveConstants withMotors(DCMotor driveMotor, DCMotor turningMotor) {
        this.DRIVE_MOTOR = driveMotor;
        this.TURNING_MOTOR = turningMotor;
        return this;
    }

    public SwerveConstants withGearRatio(GearRatio gearRatio) {
        this.DRIVE_GEAR_RATIO = gearRatio;
        return this;
    }

    public SwerveConstants withPyshicalParameters(double with,
            double length, double robotMass, WheelType wheelType) {
        this.WHEEL_TYPE = wheelType;
        this.WIDTH = with;
        this.LENGTH = length;
        this.ROBOT_MASS = robotMass;
        return this;
    }

    public SwerveConstants withTurningTuning(double Kp,
            double Ki, double Kd, double Ks, double Kv, double Ka) {
        this.TURNING_kP = Kp;
        this.TURNING_kI = Ki;
        this.TURNING_kD = Kd;
        this.TURNING_kS = Ks;
        this.TURNING_kV = Kv;
        this.TURNING_kA = Ka;
        return this;
    }

    public SwerveConstants withDriveTuning(double Kp,
            double Ki, double Kd, double Ks, double Kv, double Ka) {
        this.TELEOP_DRIVE_kP = Kp;
        this.TELEOP_DRIVE_kI = Ki;
        this.TELEOP_DRIVE_kD = Kd;
        this.TELEOP_DRIVE_kS = Ks;
        this.TELEOP_DRIVE_kV = Kv;
        this.TELEOP_DRIVE_kA = Ka;
        this.AUTO_DRIVE_kP = Kp;
        this.AUTO_DRIVE_kI = Ki;
        this.AUTO_DRIVE_kD = Kd;
        this.AUTO_DRIVE_kS = Ks;
        this.AUTO_DRIVE_kV = Kv;
        this.AUTO_DRIVE_kA = Ka;
        return this;
    }

    public SwerveConstants withDriveTuningTeleop(double Kp,
            double Ki, double Kd, double Ks, double Kv, double Ka) {
        this.TELEOP_DRIVE_kP = Kp;
        this.TELEOP_DRIVE_kI = Ki;
        this.TELEOP_DRIVE_kD = Kd;
        this.TELEOP_DRIVE_kS = Ks;
        this.TELEOP_DRIVE_kV = Kv;
        this.TELEOP_DRIVE_kA = Ka;
        return this;
    }

    public SwerveConstants withDriveAuto(double Kp,
            double Ki, double Kd, double Ks, double Kv, double Ka) {
        this.AUTO_DRIVE_kP = Kp;
        this.AUTO_DRIVE_kI = Ki;
        this.AUTO_DRIVE_kD = Kd;
        this.AUTO_DRIVE_kS = Ks;
        this.AUTO_DRIVE_kV = Kv;
        this.AUTO_DRIVE_kA = Ka;
        return this;
    }

    public SwerveConstants withTurningCurrentLimit(double currentLimit, boolean enableCurrentLimit) {
        this.TURNING__CURRENT_LIMIT = currentLimit;
        this.TURNING_ENABLE_CURRENT_LIMIT = enableCurrentLimit;
        return this;
    }

    public SwerveConstants withDriveCurrentLimit(double slipLimit, boolean enableCurrentLimit) {
        this.DRIVE__SLIP_LIMIT = slipLimit;
        this.DRIVE_ENABLE_CURRENT_LIMIT = enableCurrentLimit;
        return this;
    }

    public SwerveConstants withMaxVelocityMaxAcceleration(double maxVelocity, double maxAcceleration) {
        this.MAX_VELOCITY = maxVelocity;
        this.MAX_ACCELERATION = maxAcceleration;
        this.MAX_ANGULAR_VELOCITY = MAX_VELOCITY / RADIUS;
        return this;
    }

    public SwerveConstants withOdometryUpdateRate(double odometryUpdateRate) {
        this.ODOMETRY_UPDATE_RATE = odometryUpdateRate;
        return this;
    }

    public SwerveConstants withPPControllers(PPHolonomicDriveController realPPController,
            PPHolonomicDriveController simPPController) {
        this.realPPController = realPPController;
        this.simPPController = simPPController;
        return this;
    }

    public SwerveConstants withOptimize(boolean optimize) {
        this.OPTIMIZE = optimize;
        return this;
    }

    public static int getControlSlot() {
        return DriverStation.isAutonomous() ? AUTO_SLOT_CONFIG : TELEOP_SLOT_CONFIG;
    }

    public SwerveModule[] getModules() {
        return RobotBase.isReal() ? new SwerveModule[] {
                new SwerveModule("Front Left", this, new SwerveModuleTalonFX(this, 0)),
                new SwerveModule("Front Right", this, new SwerveModuleTalonFX(this, 1)),
                new SwerveModule("Rear Left", this, new SwerveModuleTalonFX(this, 2)),
                new SwerveModule("Rear Right", this, new SwerveModuleTalonFX(this, 3)) }
                : new SwerveModule[] {
                        new SwerveModule("Front Left", this,
                                new SwerveModuleSim(this, 0, SWERVE_DRIVE_SIMULATION.getModules()[0])),
                        new SwerveModule("Front Right", this,
                                new SwerveModuleSim(this, 1, SWERVE_DRIVE_SIMULATION.getModules()[1])),
                        new SwerveModule("Rear Left", this,
                                new SwerveModuleSim(this, 2, SWERVE_DRIVE_SIMULATION.getModules()[2])),
                        new SwerveModule("Rear Right", this,
                                new SwerveModuleSim(this, 3, SWERVE_DRIVE_SIMULATION.getModules()[3])) };
    }

    public Gyro getGyro() {
        return RobotBase.isReal() ? new Gyro("Piegon", new GyroPiegon(this)) : new Gyro("Sim Gyro", new GyroSim(this));
    }
}
