
package com.MAutils.Subsystems.DeafultSubsystems.Constants;


public class VelocitySystemConstants extends PowerSystemConstants {
    
    public double P_GAIN = 0;
    public double I_GAIN = 0;
    public double D_GAIN = 0;
    public double KS = 0;
    public double KV = 0;
    public double MAX_VELOCITY = 0;
    public double TOLERANCE = 0;

    public VelocitySystemConstants() {
        super();
        VELOCITY_FACTOR = 60;// Deafult RPM
        POSITION_FACTOR = 360;// Default Degrees
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
