
package com.MAutils.Swerve.Utils;

import java.util.function.Supplier;

import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class SwerveState {

    private ChassisSpeeds stateSpeeds;
    private String stateName;
    private Supplier<ChassisSpeeds> xySupplier;
    private Supplier<ChassisSpeeds> omegaSupplier;
    private Runnable onStateEnter = () -> {};
    private Runnable onStateRuning = () -> {};

    public SwerveState(String name) {
        this.stateName = name;
        this.stateSpeeds = new ChassisSpeeds(0, 0, 0);
    }

    public SwerveState withOnStateEnter(Runnable onStateEnter) {
        this.onStateEnter = onStateEnter;
        return this;
    }

    public SwerveState withOnStateRuning(Runnable onStateRuning) {
        this.onStateRuning = onStateRuning;
        return this;
    }

    public Runnable getOnStateEnter() {
        return onStateEnter;
    }
    
    public Runnable getOnStateRuning() {
        return onStateRuning;
    }

    public SwerveState withXY(SwerveController controller) {
        xySupplier = () -> controller.getSpeeds();
        return this;
    }

    public SwerveState withOmega(SwerveController controller) {
        omegaSupplier = () -> controller.getSpeeds();
        return this;
    }

    public SwerveState withSpeeds(SwerveController controller) {
        xySupplier = () -> controller.getSpeeds();
        omegaSupplier = () -> controller.getSpeeds();
        return this;
    }

    public SwerveState withSpeeds(ChassisSpeeds speeds) {
        xySupplier = () -> speeds;
        omegaSupplier = () -> speeds;
        return this;
    }

    public SwerveState withXY(double x, double y) {
        xySupplier = () -> new ChassisSpeeds(x, y, 0);
        return this;
    }

    public SwerveState withOmega(double omega) {
        omegaSupplier = () -> new ChassisSpeeds(0, 0, omega);
        return this;
    }

    public SwerveState withXY(Supplier<Double> x, Supplier<Double> y) {
        xySupplier = () -> new ChassisSpeeds(x.get(), y.get(), 0);
        return this;
    }

    public SwerveState withOmega(Supplier<Double> omega) {
        omegaSupplier = () -> new ChassisSpeeds(0, 0, omega.get());
        return this;
    }

    public ChassisSpeeds getSpeeds() {
        stateSpeeds = xySupplier.get();
        stateSpeeds.omegaRadiansPerSecond = omegaSupplier.get().omegaRadiansPerSecond;
        return stateSpeeds;
    }

    public String getStateName() {
        return stateName;
    }

}
