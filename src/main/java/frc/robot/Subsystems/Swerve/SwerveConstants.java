
package frc.robot.Subsystems.Swerve;

import com.MAutils.Swerve.SwerveSystemConstants;
import com.MAutils.Swerve.SwerveSystemConstants.GearRatio;
import com.MAutils.Swerve.SwerveSystemConstants.WheelType;

import edu.wpi.first.math.system.plant.DCMotor;
import frc.robot.PortMap;

public class SwerveConstants {

    public static final SwerveSystemConstants SWERVE_CONSTANTS = new SwerveSystemConstants()
    .withPyshicalParameters(0.6, 0.6, 52, WheelType.BLACK_TREAD, 6.25)
    .withMotors(DCMotor.getKrakenX60(1), DCMotor.getFalcon500(1), PortMap.Swerve.SWERVE_MODULE_IDS, PortMap.Swerve.PIGEON2)
    .withMaxVelocityMaxAcceleration(4.92, 10)
    .withOdometryUpdateRate(250)
    .withDriveCurrentLimit(120, true)//45
    .withTurningCurrentLimit(30, true)
    .withGearRatio(GearRatio.L2)
    .withDriveTuning(0, 0, 0, 0, 0.857, 0)
    .withTurningTuning(93, 0, 0, 0, 0, 0);


}
