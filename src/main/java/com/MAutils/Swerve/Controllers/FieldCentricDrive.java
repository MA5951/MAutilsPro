
package com.MAutils.Swerve.Controllers;

import java.util.function.Supplier;

import com.MAutils.Controllers.MAController;
import com.MAutils.Logger.MALog;
import com.MAutils.Swerve.SwerveSystem;
import com.MAutils.Swerve.SwerveSystemConstants;
import com.MAutils.Swerve.Utils.SwerveController;
import com.MAutils.Utils.ChassisSpeedsUtil;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class FieldCentricDrive extends SwerveController{

    private MAController controller;
    private final SwerveSystemConstants constants;
    private ChassisSpeeds speeds = new ChassisSpeeds();
    private Supplier<Boolean> reductionBoolean;
    private double reductionFactor = 1.0;

    private double xyScaler = 1;
    private double omegaScaler = 1;
    
    private Supplier<Double> angleSupplier;
    private double angleOffset = 90;

    public FieldCentricDrive(MAController controller, SwerveSystem system, SwerveSystemConstants constants) {
        super("Field Centric Drive");
        this.constants = constants;
        this.controller = controller;
        this.angleSupplier = () -> system.getGyroData().yaw;
    }

    public FieldCentricDrive withReduction(Supplier<Boolean> reductionBoolean, double reductionFactor) {
        this.reductionBoolean = reductionBoolean;
        this.reductionFactor = reductionFactor;
        return this;
    }

    public FieldCentricDrive withSclers(double xyScaler, double omegaScaler) {
        this.omegaScaler = omegaScaler;
        this.xyScaler = xyScaler;
        return this;
    }

    public void updateHeading() {
        angleOffset = angleSupplier.get();
    }

    public ChassisSpeeds getSpeeds() {
        speeds.vxMetersPerSecond = controller.withDeadbound(-controller.getLeftY()) * xyScaler * constants.MAX_VELOCITY;
        speeds.vyMetersPerSecond = controller.withDeadbound(-controller.getLeftX()) * xyScaler * constants.MAX_VELOCITY;
        speeds.omegaRadiansPerSecond = controller.withDeadbound(-controller.getRightX()) * omegaScaler * constants.MAX_ANGULAR_VELOCITY;

        if (reductionBoolean != null && reductionBoolean.get()) {
            speeds.vxMetersPerSecond *= reductionFactor;
            speeds.vyMetersPerSecond *= reductionFactor;
            speeds.omegaRadiansPerSecond *= reductionFactor;
        }

        speeds = ChassisSpeedsUtil.FromFieldToRobot(speeds, Rotation2d.fromDegrees(angleSupplier.get() - angleOffset));

        logController();

        return speeds;
    }

    private void logController() {
        MALog.log("/Subsystems/Swerve/Controllers/FieldCentricDrive/Speeds", speeds);
        MALog.log("/Subsystems/Swerve/Controllers/FieldCentricDrive/On Reduction", reductionBoolean.get());
        MALog.log("/Subsystems/Swerve/Controllers/FieldCentricDrive/Angle Offset", angleOffset);
    }


}
