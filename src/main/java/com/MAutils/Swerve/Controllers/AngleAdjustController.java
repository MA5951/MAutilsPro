
package com.MAutils.Swerve.Controllers;

import java.util.function.Supplier;

import com.MAutils.Logger.MALog;
import com.MAutils.Swerve.Utils.SwerveController;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class AngleAdjustController extends SwerveController {

    private PIDController pidController;
    private Supplier<Double> angleSupplier;

    public AngleAdjustController(PIDController pidController, Supplier<Double> angleSupplier) {
        super("Angle Adjust Controller");
        this.pidController = pidController;
        this.angleSupplier = angleSupplier;
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
        pidController.setSetpoint(setPoint);
        return this;
    }

    public ChassisSpeeds getSpeeds() {
        speeds.omegaRadiansPerSecond = pidController.calculate(angleSupplier.get());
        logController();
        return speeds;
    }

    public boolean atSetpoint() {
        return pidController.atSetpoint();
    }

    private void logController() {
        MALog.log("/Subsystems/Swerve/Controllers/AngleAdjustController/Speeds", speeds);
        MALog.log("/Subsystems/Swerve/Controllers/AngleAdjustController/Set Point", pidController.getSetpoint());
        MALog.log("/Subsystems/Swerve/Controllers/AngleAdjustController/At Point", pidController.atSetpoint());
    }
}
