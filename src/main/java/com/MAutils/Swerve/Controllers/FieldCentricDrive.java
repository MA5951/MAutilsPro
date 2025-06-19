
package com.MAutils.Swerve.Controllers;

import java.util.function.Supplier;

import com.MAutils.Swerve.SwerveSystem;
import com.MAutils.Swerve.SwerveSystemConstants;
import com.MAutils.Swerve.Utils.SwerveController;
import com.MAutils.Utils.ChassisSpeedsUtil;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.XboxController;

public class FieldCentricDrive extends SwerveController {

    private ChassisSpeeds chassisSpeeds = new ChassisSpeeds();
    private XboxController controller;
    private Supplier<Boolean> reductionBoolean;
    private Supplier<Double> angleSupplier;
    private double reductionPrecent = 1;
    private double xSpeed;
    private double ySpeed;
    private double turningSpeed;
    private double gyroOffset = 0d;
    private double xyScaler = 1;
    private double thetaScaler = 1;
    private final SwerveSystemConstants SwerveConstants;

    public FieldCentricDrive(XboxController Controller, Supplier<Boolean> reductionSupplier,
            double ReductionPrecent,
            SwerveSystem swerveSystem, SwerveSystemConstants SwerveSystemConstants) {
        super("FIeldCentricDrive");
        SwerveConstants = SwerveSystemConstants;
        controller = Controller;
        reductionBoolean = reductionSupplier;
        reductionPrecent = ReductionPrecent;
        angleSupplier = () -> swerveSystem.getGyroData().yaw;

    }

    public ChassisSpeeds getSpeeds() {
        xSpeed = controller.getLeftX();
        ySpeed = controller.getLeftY();
        turningSpeed = controller.getRightX();

        xSpeed = Math.abs(xSpeed) < 0.1 ? 0 : -xSpeed * xyScaler * SwerveConstants.MAX_VELOCITY;
        ySpeed = Math.abs(ySpeed) < 0.1 ? 0 : -ySpeed * xyScaler * SwerveConstants.MAX_VELOCITY;
        turningSpeed = Math.abs(turningSpeed) < 0.1 ? 0
                : -turningSpeed * thetaScaler
                        * SwerveConstants.MAX_ANGULAR_VELOCITY;

        chassisSpeeds.vxMetersPerSecond = reductionBoolean.get() ? ySpeed * reductionPrecent : ySpeed;
        chassisSpeeds.vyMetersPerSecond = reductionBoolean.get() ? xSpeed * reductionPrecent : xSpeed;
        chassisSpeeds.omegaRadiansPerSecond = turningSpeed;

        chassisSpeeds = ChassisSpeedsUtil.FromFieldToRobot(chassisSpeeds, new Rotation2d(
                Math.toRadians((angleSupplier.get() - gyroOffset))));

        System.out.println(xSpeed);

        return chassisSpeeds;

    }

    public void updateDriveHeading() {
        gyroOffset = angleSupplier.get();
    }

    public double getGyroOffset() {
        return gyroOffset;
    }
}