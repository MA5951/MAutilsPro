
package frc.robot.Subsystems.Swerve;

import com.MAutils.Swerve.SwerveSystem;
import com.MAutils.Swerve.SwerveSystemConstants;
import com.MAutils.Swerve.Controllers.FieldCentricDrive;
import com.MAutils.Swerve.SwerveSystemConstants.GearRatio;
import com.MAutils.Swerve.SwerveSystemConstants.WheelType;
import com.MAutils.Swerve.Utils.SwerveState;

import edu.wpi.first.math.system.plant.DCMotor;
import frc.robot.PortMap;
import frc.robot.RobotContainer;

public class SwerveConstants {

    public static final SwerveSystemConstants SWERVE_CONSTANTS = new SwerveSystemConstants()
            .withPyshicalParameters(0.6, 0.6, 52, WheelType.BLACK_TREAD, 6.25)
            .withMotors(DCMotor.getKrakenX60(1), DCMotor.getFalcon500(1), PortMap.Swerve.SWERVE_MODULE_IDS,
                    PortMap.Swerve.PIGEON2)
            .withMaxVelocityMaxAcceleration(4.92, 10)
            .withOdometryUpdateRate(250)
            .withDriveCurrentLimit(120, true)// 45
            .withTurningCurrentLimit(30, true)
            .withGearRatio(GearRatio.L2);

    public static final FieldCentricDrive fieldCentricController = new FieldCentricDrive(
            RobotContainer.getDriverController(),
            SwerveSystem.getInstance(SwerveConstants.SWERVE_CONSTANTS),
            SWERVE_CONSTANTS);

    public static final SwerveState FIELD_DRIVE = new SwerveState("Field Drive").withSpeeds(fieldCentricController);

}
