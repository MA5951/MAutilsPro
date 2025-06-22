
package com.MAutils.Swerve.Controllers;

import java.util.function.Supplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class XYAdjustController {

    private PIDController xController;
    private PIDController yController;
    private Supplier<Double> xSupplier;
    private Supplier<Double> ySupplier;
    private ChassisSpeeds speeds = new ChassisSpeeds();

    public XYAdjustController(PIDController xController, PIDController yController,
            Supplier<Double> xSupplier, Supplier<Double> ySupplier) {
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
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
        return this;
    }

    public XYAdjustController withXSetPoint(double xSetPoint, double ySetPoint) {
        xController.setSetpoint(xSetPoint);
        yController.setSetpoint(ySetPoint);
        return this;
    }

    public ChassisSpeeds getSpeeds() {
        speeds.vxMetersPerSecond = xController.calculate(xSupplier.get());
        speeds.vyMetersPerSecond = yController.calculate(ySupplier.get());
        logController();
        return speeds;
    }

    public boolean atSetpoint() {
        return xController.atSetpoint() && yController.atSetpoint();
    }

    private void logController() {
        System.out.println("Speeds: " + speeds);
        System.out.println("X Set Point: " + xController.getSetpoint());
        System.out.println("Y Set Point: " + yController.getSetpoint());
        System.out.println("At Set Point: " + atSetpoint());
    }
}
