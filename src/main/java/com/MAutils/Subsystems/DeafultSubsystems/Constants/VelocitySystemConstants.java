
package com.MAutils.Subsystems.DeafultSubsystems.Constants;

import com.MAutils.Components.Motor;

public class VelocitySystemConstants extends DeafultSystemConstants<VelocitySystemConstants> {
    
    public double P_GAIN = 0;
    public double I_GAIN = 0;
    public double D_GAIN = 0;
    public double KS = 0;
    public double KV = 0;
    public double MAX_VELOCITY = 0;
    public double TOLERANCE = 0;

    public VelocitySystemConstants(Motor... motors) {
        super(motors);
    }

    public VelocitySystemConstants withPID(double pGain, double iGain, double dGain, double tolerance) {
        this.P_GAIN = pGain;
        this.I_GAIN = iGain;
        this.D_GAIN = dGain;
        this.TOLERANCE = tolerance;
        return this;
    }

    public VelocitySystemConstants withMaxVelocity(double maxVelocity) {
        this.MAX_VELOCITY = maxVelocity;
        return this;
    }

    public VelocitySystemConstants withFeedForward(double kS, double kV) {
        this.KS = kS;
        this.KV = kV;
        return this;
    }
}
