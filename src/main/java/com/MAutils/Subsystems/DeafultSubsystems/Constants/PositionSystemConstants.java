
package com.MAutils.Subsystems.DeafultSubsystems.Constants;



import com.MAutils.Components.Motor;
import com.MAutils.Utils.GainConfig;

public class PositionSystemConstants extends DeafultSystemConstants<PositionSystemConstants> {
    
    
    public GainConfig realGainConfig = new GainConfig(); //TODO private 
    public GainConfig simGainConfig = new GainConfig().withKP(1); //TODO final
    public double MIN_POSE = 0; //TODO final/private
    public double MAX_POSE = 0; //TODO final/private
    public double START_POSE = 0; //TODO final /private
    public double TOLERANCE = 0; //TODO final /private
    public double CRUISE_VELOCITY = 0; //TODO can be calculate
    public double ACCELERATION = 0; //TODO can be calculate
    public double JERK = 0; //TODO final /private
    public boolean IS_MOTION_MAGIC = false;
    public double MASS = -1; //TODO final/private

     //TODO add is Continuous
     //TODO add deadband for postion

    public PositionSystemConstants(String name, double minPose, double maxPose, double startPose,double tolerance ,Motor master,Motor... motors) {
        super(name, master,motors);
        this.MIN_POSE = minPose;
        this.MAX_POSE = maxPose;
        this.START_POSE = startPose;
        this.TOLERANCE = tolerance;
    }

    public PositionSystemConstants withTolerance(double tolerance) { 
        this.TOLERANCE = tolerance;
        return this;
    }

    public PositionSystemConstants withRealGains(GainConfig gainConfig) {
        this.realGainConfig = gainConfig; //TODO this shoould be in the constructor
        return this;
    }

    public PositionSystemConstants withSimGains(GainConfig gainConfig) {
        this.simGainConfig = gainConfig;
        return this;
    }

    //TODO split this to 3 func use motion magic cruise veloctiy and acceleration and jerk 
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
        return simGainConfig; //TODO what about the redl gain?
    }


    // public GainConfig getGainConfig() {
    //     return Robot.isReal() ? realGainConfig : simGainConfig;
    // }

}
