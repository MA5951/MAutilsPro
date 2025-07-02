
package com.MAutils.Subsystems.DeafultSubsystems.Constants;

import com.MAutils.Components.Motor;
import com.MAutils.Utils.GainConfig;


public class VelocitySystemConstants extends DeafultSystemConstants<VelocitySystemConstants> {
    
    public GainConfig realGainConfig = new GainConfig();
    public GainConfig simGainConfig = new GainConfig()
    .withKP(1);
    public double MAX_VELOCITY = 0;
    public double TOLERANCE = 0;
    public double WHEEL_RADIUS = 0;


    public VelocitySystemConstants(double maxVelocity, double tolerance ,Motor master,Motor... motors) {
        super(master, motors);
        this.MAX_VELOCITY = maxVelocity;
        this.TOLERANCE = tolerance;
    }


    public VelocitySystemConstants withRealGains(GainConfig gainConfig) {
        this.realGainConfig = gainConfig;
        return this;
    }

    public VelocitySystemConstants withSimGains(GainConfig gainConfig) {
        this.simGainConfig = gainConfig;
        return this;
    }

    public GainConfig getGainConfig() {
        return simGainConfig;
    }

    // public GainConfig getGainConfig() {
    //     return Robot.isReal() ? realGainConfig : simGainConfig;
    // }

    public VelocitySystemConstants withWheelRadius(double wheelRadius) {
        this.WHEEL_RADIUS = wheelRadius;
        return this;
    }

    public VelocitySystemConstants withTolerance(double tolerance) {
        this.TOLERANCE = tolerance;
        return this;
    }
}
