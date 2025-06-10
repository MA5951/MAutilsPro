
package com.MAutils.Subsystems.DeafultSubsystems.Constants;


public class PositionSystemConstants extends PowerSystemConstants {
    
    public double P_GAIN = 0;
    public double I_GAIN = 0;
    public double D_GAIN = 0;
    public double F_GAIN = 0;
    public double MIN_POSE = 0;
    public double MAX_POSE = 0;
    public double START_POSE = 0;
    public double TOLERANCE = 0;
    public double CRUISE_VELOCITY = 0;
    public double ACCELERATION = 0;
    public double JERK = 0;
    public boolean IS_MOTION_MAGIC = false;

    public PositionSystemConstants() {
        super();
        VELOCITY_FACTOR = 60;// Default RPM
        POSITION_FACTOR = 360;// Default Degrees
    }

    public PositionSystemConstants withPID(double pGain, double iGain, double dGain, double tolerance) {
        this.P_GAIN = pGain;
        this.I_GAIN = iGain;
        this.D_GAIN = dGain;
        this.TOLERANCE = tolerance;
        return this;
    }

    public PositionSystemConstants withPoseLimits(double minPose, double maxPose, double startPose) {
        this.MIN_POSE = minPose;
        this.MAX_POSE = maxPose;
        this.START_POSE = startPose;
        return this;
    }

    public PositionSystemConstants withMotionMagic(double cruiseVelocity, double acceleration, double jerk) {
        this.CRUISE_VELOCITY = cruiseVelocity;
        this.ACCELERATION = acceleration;
        this.JERK = jerk;
        this.IS_MOTION_MAGIC = true;
        return this;
    }

    public PositionSystemConstants withFeedForward(double kF) {
        this.F_GAIN = kF;
        return this;
    }

}
