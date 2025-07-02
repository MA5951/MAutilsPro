
package com.MAutils.Subsystems.DeafultSubsystems.Constants;



import com.MAutils.Components.Motor;
import com.MAutils.Utils.GainConfig;

public class PositionSystemConstants extends DeafultSystemConstants<PositionSystemConstants> {
    
    public GainConfig realGainConfig = new GainConfig();
    public GainConfig simGainConfig = new GainConfig().withKP(1);
    public double MIN_POSE = 0;
    public double MAX_POSE = 0;
    public double START_POSE = 0;
    public double TOLERANCE = 0;
    public double CRUISE_VELOCITY = 0;
    public double ACCELERATION = 0;
    public double JERK = 0;
    public boolean IS_MOTION_MAGIC = false;
    public double MASS = -1;

    public PositionSystemConstants(double minPose, double maxPose, double startPose,double tolerance ,Motor master,Motor... motors) {
        super(master,motors);
        this.MIN_POSE = minPose;
        this.MAX_POSE = maxPose;
        this.START_POSE = startPose;
        this.TOLERANCE = tolerance;
        this.INERTIA = 0.000000001;
    }

    public PositionSystemConstants withTolerance(double tolerance) {
        this.TOLERANCE = tolerance;
        return this;
    }

    public PositionSystemConstants withRealGains(GainConfig gainConfig) {
        this.realGainConfig = gainConfig;
        return this;
    }

    public PositionSystemConstants withSimGains(GainConfig gainConfig) {
        this.simGainConfig = gainConfig;
        return this;
    }

    public PositionSystemConstants withMotionMagic(double cruiseVelocity, double acceleration, double jerk) {
        this.CRUISE_VELOCITY = cruiseVelocity;
        this.ACCELERATION = acceleration;
        this.JERK = jerk;
        this.IS_MOTION_MAGIC = true;
        return this;
    }

    public PositionSystemConstants withMass(double mass) {
        this.MASS = mass;
        return this;
    }

    public GainConfig getGainConfig() {
        return simGainConfig;
    }

    // public GainConfig getGainConfig() {
    //     return Robot.isReal() ? realGainConfig : simGainConfig;
    // }

}
