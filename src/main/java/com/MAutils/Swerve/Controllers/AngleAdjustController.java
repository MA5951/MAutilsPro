
package com.MAutils.Swerve.Controllers;

import java.util.function.Supplier;

import com.MAutils.Logger.MALog;
import com.MAutils.Swerve.Utils.SwerveController;

import edu.wpi.first.math.controller.PIDController;

public class AngleAdjustController extends SwerveController {

    private PIDController pidController;
    private Supplier<Double> angleSupplier;
    private double angleOffset = 0;

    public AngleAdjustController(PIDController pidController, Supplier<Double> angleSupplier) {
        super("Angle Adjust Controller");
        this.pidController = pidController;
        this.angleSupplier = angleSupplier;

        pidController.setSetpoint(angleOffset);
    }

    public AngleAdjustController withAngleSupplier(Supplier<Double> angleSupplier) {
        this.angleSupplier = angleSupplier;
        return this;
    }

    public AngleAdjustController withPIDController(PIDController pidController) {
        this.pidController = pidController;
        return this;
    }

    public AngleAdjustController withSetPoint(double setPoint) {
        pidController.setSetpoint(setPoint + angleOffset);
        return this;
    }

    public AngleAdjustController withSetPoint(Supplier<Double> setPoint) {
        pidController.setSetpoint(setPoint.get() + angleOffset);
        return this;
    }

    public AngleAdjustController withAngleOffset(double angleOffset) {
        this.angleOffset = angleOffset;
        return this;
    }

    public void updateSpeeds() {
        speeds.omegaRadiansPerSecond = pidController.calculate(angleSupplier.get());
    }

    public boolean atSetpoint() {
        return pidController.atSetpoint();
    }

    public double getSetpoint() {
        return pidController.getSetpoint();
    }

    @Override
    public void logController() {
        super.logController();
        MALog.log("/Subsystems/Swerve/Controllers/AngleAdjustController/Set Point", pidController.getSetpoint());
        MALog.log("/Subsystems/Swerve/Controllers/AngleAdjustController/At Point", pidController.atSetpoint());
    }
}
