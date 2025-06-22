
package com.MAutils.Subsystems.DeafultSubsystems.Constants;

import com.MAutils.Components.Motor;
import com.MAutils.Utils.GainConfig;

import frc.robot.Robot;

public class VelocitySystemConstants extends DeafultSystemConstants<VelocitySystemConstants> {
    
    public GainConfig realGainConfig = new GainConfig();
    public GainConfig simGainConfig = new GainConfig()
    .withKP(1);;
    public double MAX_VELOCITY = 0;
    public double TOLERANCE = 0;

    public VelocitySystemConstants(double maxVelocity ,Motor... motors) {
        super(motors);
        this.MAX_VELOCITY = maxVelocity;
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
        return Robot.isReal() ? realGainConfig : simGainConfig;
    }

    public VelocitySystemConstants withTolerance(double tolerance) {
        this.TOLERANCE = tolerance;
        return this;
    }
}
