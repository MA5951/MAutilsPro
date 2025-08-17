package com.MAutils.Subsystems.DeafultSubsystems.Constants;

import com.MAutils.Components.Motor;
import com.MAutils.Utils.GainConfig;

public class PositionSystemConstants extends DeafultSystemConstants<PositionSystemConstants> {

    public GainConfig realGainConfig = new GainConfig();
    public GainConfig simGainConfig  = new GainConfig().withKP(1);

    public double MIN_POSE = 0;
    public double MAX_POSE = 0;
    public double START_POSE = 0;
    public double TOLERANCE = 0;

    public double CRUISE_VELOCITY = 0;
    public double ACCELERATION = 0;
    public double JERK = 0;
    public boolean IS_MOTION_MAGIC = false;

    public double MASS = -1;

    /** Primary ctor used by the builder. */
    public PositionSystemConstants(
            DeafultSystemConstants.Builder b,
            double minPose,
            double maxPose,
            double startPose,
            double tolerance,
            GainConfig realGains,
            GainConfig simGains,
            boolean isMotionMagic,
            double cruiseVelocity,
            double acceleration,
            double jerk,
            double mass
    ) {
        super(b);
        this.MIN_POSE = minPose;
        this.MAX_POSE = maxPose;
        this.START_POSE = startPose;
        this.TOLERANCE = tolerance;

        this.realGainConfig = (realGains != null) ? realGains : new GainConfig();
        this.simGainConfig  = (simGains  != null) ? simGains  : new GainConfig().withKP(1);

        this.IS_MOTION_MAGIC = isMotionMagic;
        this.CRUISE_VELOCITY = cruiseVelocity;
        this.ACCELERATION    = acceleration;
        this.JERK            = jerk;

        this.MASS = mass;
    }

    /* optional post-build mutators (back-compat) */
    public PositionSystemConstants withTolerance(double tolerance) {
        this.TOLERANCE = tolerance;
        return this;
    }
    public PositionSystemConstants withRealGains(GainConfig gainConfig) { this.realGainConfig = gainConfig; return this; }
    public PositionSystemConstants withSimGains(GainConfig gainConfig) { this.simGainConfig = gainConfig; return this; }
    public PositionSystemConstants withMotionMagic(double cruiseVelocity, double acceleration, double jerk) {
        this.CRUISE_VELOCITY = cruiseVelocity; this.ACCELERATION = acceleration; this.JERK = jerk; this.IS_MOTION_MAGIC = true; return this;
    }
    public PositionSystemConstants withMass(double mass) { this.MASS = mass; return this; }

    public GainConfig getGainConfig() { return simGainConfig; }

    /* =======================
       Subclass Builder
       ======================= */

    /** Use a distinct name to avoid hiding the base static builder. */
    public static Builder newBuilder(String name, Motor master, Motor... motors) {
        return new Builder(name, master, motors);
    }

    public static final class Builder {
        private final DeafultSystemConstants.Builder base;

        private double minPose = 0;
        private double maxPose = 0;
        private double startPose = 0;
        private double tolerance = 0;

        private GainConfig realGains = new GainConfig();
        private GainConfig simGains  = new GainConfig().withKP(1);

        private boolean motionMagic = false;
        private double cruiseVelocity = 0;
        private double acceleration = 0;
        private double jerk = 0;

        private double mass = -1;

        private Builder(String name, Motor master, Motor... motors) {
            this.base = DeafultSystemConstants.builder(name, master, motors);
        }

        /* subclass options */
        public Builder range(double minPose, double maxPose) { this.minPose = minPose; this.maxPose = maxPose; return this; }
        public Builder startPose(double startPose) { this.startPose = startPose; return this; }
        public Builder tolerance(double tolerance) { this.tolerance = tolerance; return this; }

        public Builder realGains(GainConfig gains) { this.realGains = gains; return this; }
        public Builder simGains(GainConfig gains) { this.simGains = gains; return this; }

        public Builder motionMagic(double cruiseVel, double accel, double jerk) {
            this.motionMagic = true; this.cruiseVelocity = cruiseVel; this.acceleration = accel; this.jerk = jerk; return this;
        }

        public Builder mass(double mass) { this.mass = mass; return this; }

        /* delegate base builder for fluent chaining */
        public Builder motorCurrentLimit(double amps) { base.motorCurrentLimit(amps); return this; }
        public Builder gear(double gear) { base.gear(gear); return this; }
        public Builder statorCurrentLimit(boolean enabled, double limit) { base.statorCurrentLimit(enabled, limit); return this; }
        public Builder peakVoltage(double forward, double reverse) { base.peakVoltage(forward, reverse); return this; }
        public Builder isBrake(boolean brake) { base.isBrake(brake); return this; }
        public Builder positionFactor(double factor) { base.positionFactor(factor); return this; }
        public Builder velocityFactor(double factor) { base.velocityFactor(factor); return this; }
        public Builder inertia(double inertia) { base.inertia(inertia); return this; }
        public Builder foc(boolean foc) { base.foc(foc); return this; }
        public Builder logPath(String path) { base.logPath(path); return this; }

        public PositionSystemConstants build() {
            return base.build(b -> new PositionSystemConstants(
                    b,
                    minPose, maxPose, startPose, tolerance,
                    realGains, simGains,
                    motionMagic, cruiseVelocity, acceleration, jerk,
                    mass
            ));
        }
    }
}
