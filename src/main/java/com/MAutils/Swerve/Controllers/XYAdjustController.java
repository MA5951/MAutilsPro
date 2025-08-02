
package com.MAutils.Swerve.Controllers;

import java.util.function.Supplier;

import com.MAutils.Logger.MALog;
import com.MAutils.Swerve.SwerveSystem;
import com.MAutils.Swerve.Utils.SwerveController;
import com.MAutils.Utils.ChassisSpeedsUtil;
import com.MAutils.Utils.DriverStationUtil;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class XYAdjustController extends SwerveController {

    private PIDController xController;
    private PIDController yController;
    private Supplier<Double> xSupplier;
    private Supplier<Double> ySupplier;
    private Supplier<Double> gyroSupplier;
    private boolean fieldRelative = true;

    public XYAdjustController(PIDController xController, PIDController yController,
            Supplier<Double> xSupplier, Supplier<Double> ySupplier) {
        super("XY Adjust Controller");
        this.xController = xController;
        this.yController = yController;
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
    }

    public XYAdjustController(SwerveSystem swerveSystem, PIDController xController, PIDController yController,
            Supplier<Double> xSupplier, Supplier<Double> ySupplier) {
        super("XY Adjust Controller");
        this.xController = xController;
        this.yController = yController;
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
    }

    public XYAdjustController withGyroSupplier(Supplier<Double> gyroSupplier) {
        this.gyroSupplier = gyroSupplier;
        return this;
    }

    public XYAdjustController withFieldRelative(boolean fieldRelative) {
        this.fieldRelative = fieldRelative;
        return this;
    }

    public XYAdjustController withXYControllers(PIDController xController, PIDController yController) {
        this.xController = xController;
        this.yController = yController;
        return this;
    }

    public XYAdjustController withXYSetPoint(Supplier<Double> xSupplier, Supplier<Double> ySupplier) {
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
        return this;
    }

    public XYAdjustController withXYSetPoint(double xSetPoint, double ySetPoint) {
        this.xSupplier = () -> xSetPoint;
        this.ySupplier = () -> ySetPoint;
        return this;
    }

    public void updateSpeeds() {
        speeds.vxMetersPerSecond = xController.calculate(xSupplier.get());
        speeds.vyMetersPerSecond = yController.calculate(ySupplier.get());

        if (fieldRelative && DriverStationUtil.getAlliance() == Alliance.Blue) {
            speeds = ChassisSpeedsUtil.FromFieldToRobot(speeds, new Rotation2d(
                    Math.toRadians((-(gyroSupplier.get())))));
        } else if (fieldRelative) {
            speeds = ChassisSpeedsUtil.FromFieldToRobot(speeds, new Rotation2d(
                    Math.toRadians((-(gyroSupplier.get()) - 180))));
        }
    }

    public boolean atSetpoint() {
        return xController.atSetpoint() && yController.atSetpoint();
    }

    public Pose2d getSetPoint() {
        return new Pose2d(xController.getSetpoint(), yController.getSetpoint(), new Rotation2d());
    }

    public void logController() {
        super.logController();

        MALog.log("/Subsystems/Swerve/Controllers/XYAdjustController/X Set Point", xController.getSetpoint());
        MALog.log("/Subsystems/Swerve/Controllers/XYAdjustController/Y Set Point", yController.getSetpoint());
        MALog.log("/Subsystems/Swerve/Controllers/XYAdjustController/X At Point", xController.atSetpoint());
        MALog.log("/Subsystems/Swerve/Controllers/XYAdjustController/Y At Point", yController.atSetpoint());
    }
}
