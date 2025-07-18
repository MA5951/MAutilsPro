
package com.MAutils.Swerve.Controllers;

import java.util.function.Supplier;

import com.MAutils.Swerve.Utils.SwerveController;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class XYAdjustController extends SwerveController{

    private PIDController xController;
    private PIDController yController;
    private Supplier<Double> xSupplier;
    private Supplier<Double> ySupplier;

    public XYAdjustController(PIDController xController, PIDController yController,
            Supplier<Double> xSupplier, Supplier<Double> ySupplier) {
        super("XY Adjust Controller");
        this.xController = xController;
        this.yController = yController;
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
    }

    public XYAdjustController withXYControllers(PIDController xController, PIDController yController) {
        this.xController = xController;
        this.yController = yController;
        return this;
    }

    public XYAdjustController withXSuppliers(Supplier<Double> xSupplier, Supplier<Double> ySupplier) {
        //TODO name dont match the parmeters
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
        return this;
    }

    public XYAdjustController withXSetPoint(double xSetPoint, double ySetPoint) {
        //TODO name dont match the parmeters
        xController.setSetpoint(xSetPoint);
        yController.setSetpoint(ySetPoint);
        return this;
    }

    public ChassisSpeeds getSpeeds() {
        //TODO add setconstraints

        //TODO add gyro offset for alliance  and fild relativ or robot relativ
        speeds.vxMetersPerSecond = xController.calculate(xSupplier.get());
        speeds.vyMetersPerSecond = yController.calculate(ySupplier.get());
        logController();
        return speeds;
    }

    public boolean atSetpoint() {
        return xController.atSetpoint() && yController.atSetpoint();
    }

    //TODO add get setPoint as pose2d

    private void logController() {
        System.out.println("Speeds: " + speeds);
        System.out.println("X Set Point: " + xController.getSetpoint());
        System.out.println("Y Set Point: " + yController.getSetpoint());
        System.out.println("At Set Point: " + atSetpoint());
    }
}
