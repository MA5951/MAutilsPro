
package com.MAutils.Swerve.IOs.SwerveModule;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.Rotations;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import static edu.wpi.first.units.Units.Volts;

import java.util.Queue;

import com.MAutils.Swerve.SwerveConstants;
import com.MAutils.Utils.StatusSignalsRunner;
import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.ParentDevice;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;

public class SwerveModuleTalonFX implements SwerveModuleIO {

    protected final TalonFX driveTalon;
    protected final TalonFX turnTalon;
    protected final CANcoder cancoder;

    private final PositionVoltage positionVoltageRequest = new PositionVoltage(0.0);
    private final VelocityVoltage velocityVoltageRequest = new VelocityVoltage(0.0);

    private final TalonFXConfiguration driveTalonConfig = new TalonFXConfiguration();
    protected final TalonFXConfiguration turnTalonConfig = new TalonFXConfiguration();

    private final StatusSignal<Angle> drivePosition;
    private Queue<Double> drivePositionQueue;// TODO
    private final StatusSignal<AngularVelocity> driveVelocity;
    private final StatusSignal<Voltage> driveAppliedVolts;
    private final StatusSignal<Current> driveCurrent;

    private final StatusSignal<Angle> turnAbsolutePosition;
    private final StatusSignal<Angle> turnPosition;
    private Queue<Double> turnPositionQueue;// TODO
    private final StatusSignal<AngularVelocity> turnVelocity;
    private final StatusSignal<Voltage> turnAppliedVolts;
    private final StatusSignal<Current> turnCurrent;

    private final SwerveConstants constants;

    public SwerveModuleTalonFX(SwerveConstants constants, int index) {
        this.constants = constants;
        driveTalon = new TalonFX(constants.MODULES_ID_ARRY[index].driveMotor.id,
                constants.MODULES_ID_ARRY[index].driveMotor.bus);
        turnTalon = new TalonFX(constants.MODULES_ID_ARRY[index].steerMotor.id,
                constants.MODULES_ID_ARRY[index].steerMotor.bus);
        cancoder = new CANcoder(constants.MODULES_ID_ARRY[index].steerEncoder.id,
                constants.MODULES_ID_ARRY[index].steerEncoder.bus);

        configDevices();

        drivePosition = driveTalon.getPosition(false);
        // drivePositionQueue =
        // PhoenixOdometryThread.getInstance().registerSignal(driveTalon.getPosition().clone());//TODO
        driveVelocity = driveTalon.getVelocity(false);
        driveAppliedVolts = driveTalon.getMotorVoltage(false);
        driveCurrent = driveTalon.getStatorCurrent(false);

        turnAbsolutePosition = cancoder.getAbsolutePosition(false);

        turnPosition = turnTalon.getPosition(false);
        // turnPositionQueue =
        // PhoenixOdometryThread.getInstance().registerSignal(turnTalon.getPosition().clone());//TODO
        turnVelocity = turnTalon.getVelocity(false);
        turnAppliedVolts = turnTalon.getMotorVoltage(false);
        turnCurrent = turnTalon.getStatorCurrent(false);

        BaseStatusSignal.setUpdateFrequencyForAll(
                constants.ODOMETRY_UPDATE_RATE, drivePosition, turnPosition, turnAbsolutePosition);
        BaseStatusSignal.setUpdateFrequencyForAll(
                50.0,
                driveVelocity,
                driveAppliedVolts,
                driveCurrent,
                turnVelocity,
                turnAppliedVolts,
                turnCurrent);

        ParentDevice.optimizeBusUtilizationForAll(driveTalon, turnTalon, cancoder);

        StatusSignalsRunner.registerSignals(drivePosition, driveVelocity, driveAppliedVolts,
                driveCurrent, turnAbsolutePosition, turnPosition, turnVelocity, turnAppliedVolts, turnCurrent);

    }

    private void configDevices() {
        driveTalonConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        driveTalonConfig.Feedback.SensorToMechanismRatio = constants.DRIVE_GEAR_RATIO.gearRatio;
        driveTalonConfig.CurrentLimits.StatorCurrentLimit = constants.DRIVE__SLIP_LIMIT;
        driveTalonConfig.CurrentLimits.StatorCurrentLimitEnable = constants.DRIVE_ENABLE_CURRENT_LIMIT;

        driveTalonConfig.Slot0.kP = constants.TELEOP_DRIVE_kP;
        driveTalonConfig.Slot0.kI = constants.TELEOP_DRIVE_kI;
        driveTalonConfig.Slot0.kD = constants.TELEOP_DRIVE_kD;
        driveTalonConfig.Slot0.kS = constants.TELEOP_DRIVE_kS;
        driveTalonConfig.Slot0.kV = constants.TELEOP_DRIVE_kV;
        driveTalonConfig.Slot0.kA = constants.TELEOP_DRIVE_kA;

        driveTalonConfig.Slot1.kP = constants.AUTO_DRIVE_kP;
        driveTalonConfig.Slot1.kI = constants.AUTO_DRIVE_kI;
        driveTalonConfig.Slot1.kD = constants.AUTO_DRIVE_kD;
        driveTalonConfig.Slot1.kS = constants.AUTO_DRIVE_kS;
        driveTalonConfig.Slot1.kV = constants.AUTO_DRIVE_kV;
        driveTalonConfig.Slot1.kA = constants.AUTO_DRIVE_kA;

        driveTalon.getConfigurator().apply(driveTalonConfig);
        driveTalon.setPosition(0);

        turnTalonConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        turnTalonConfig.Feedback.SensorToMechanismRatio = constants.TURNING_GEAR_RATIO;
        turnTalonConfig.ClosedLoopGeneral.ContinuousWrap = true;
        turnTalonConfig.CurrentLimits.StatorCurrentLimit = constants.TURNING__CURRENT_LIMIT;
        turnTalonConfig.CurrentLimits.StatorCurrentLimitEnable = constants.TURNING_ENABLE_CURRENT_LIMIT;
        turnTalonConfig.Slot0.kP = constants.TURNING_kP;
        turnTalonConfig.Slot0.kI = constants.TURNING_kI;
        turnTalonConfig.Slot0.kD = constants.TURNING_kD;
        turnTalonConfig.Slot0.kS = constants.TURNING_kS;
        turnTalonConfig.Slot0.kV = constants.TURNING_kV;
        turnTalonConfig.Slot0.kA = constants.TURNING_kA;

        turnTalon.getConfigurator().apply(turnTalonConfig);
        turnTalon.setPosition(0);

    }

    public void updateSwerveModuleData(SwerveModuleData data) {
        data.isDriveConnected = BaseStatusSignal.isAllGood(
                drivePosition, driveVelocity, driveAppliedVolts, driveCurrent);
        data.drivePosition = drivePosition.getValue().in(Rotations) * constants.WHEEL_CIRCUMFERENCE;// Rotations to Meters
        data.driveVelocity = driveVelocity.getValue().in(RotationsPerSecond) * constants.WHEEL_CIRCUMFERENCE; // Rotations Per Second to Meters Per Second
        data.driveCurrent = driveCurrent.getValue().in(Amps);
        data.driveVolts = driveAppliedVolts.getValue().in(Volts);

        data.isSteerConnected = BaseStatusSignal.isAllGood(
                turnPosition, turnVelocity, turnAppliedVolts, turnCurrent);
        data.steerPosition = new Rotation2d(turnPosition.getValue().in(Radians));// Radians to Rotation2d
        data.steerVelocity = turnVelocity.getValue().in(RotationsPerSecond) * 60;// Rotations Per Second to RPM
        data.steerCurrent = turnCurrent.getValue().in(Amps);
        data.steerVolts = turnAppliedVolts.getValue().in(Volts);

        data.absoluteSteerPosition = turnAbsolutePosition.getValue().in(Radians);// Radians to Rotation2d
        data.isAbsoluteSteerConnected = BaseStatusSignal.isAllGood(turnAbsolutePosition);

        // data.odometryDrivePositionsRad = drivePositionQueue.stream()
        // .mapToDouble((Double value) -> value).toArray();// TODO
        // data.odometryTurnPositions = turnPositionQueue.stream()

        drivePositionQueue.clear();
        turnPositionQueue.clear();

    }

    public void setDriveVoltage(double volts) {
        driveTalon.setVoltage(volts);
    }

    public void setSteerVoltage(double volts) {
        turnTalon.setVoltage(volts);
    }

    public void setDriveVelocity(double metersPerSecond) {
        driveTalon.setControl(velocityVoltageRequest.withVelocity(metersPerSecond / constants.WHEEL_CIRCUMFERENCE)// Meters Per Second to Rotations Per Second
                .withSlot(SwerveConstants.getControlSlot()));
    }

    public void setSteerPosition(Rotation2d rotation) {
        turnTalon.setControl(positionVoltageRequest.withPosition(rotation.getRotations()));// Rotation2d to Rotations
    }

    public void setDrivePID(double kP, double kI, double kD) {
        driveTalonConfig.Slot0.kP = kP;
        driveTalonConfig.Slot0.kI = kI;
        driveTalonConfig.Slot0.kD = kD;
        driveTalon.getConfigurator().apply(driveTalonConfig);
    }

    public void setSteerPID(double kP, double kI, double kD) {
        turnTalonConfig.Slot0.kP = kP;
        turnTalonConfig.Slot0.kI = kI;
        turnTalonConfig.Slot0.kD = kD;
        turnTalon.getConfigurator().apply(turnTalonConfig);
    }
}
