package com.ma5951.utils.subsystems;

/**
 * Configuration specific to a roller subsystem.
 */
public class RollerSystemConstants extends BaseSystemConstants {
    private final double idleSpeed;
    private final double intakeSpeed;

    private RollerSystemConstants(Builder builder) {
        super(builder);
        this.idleSpeed = builder.idleSpeed;
        this.intakeSpeed = builder.intakeSpeed;
    }

    public double getIdleSpeed() { return idleSpeed; }
    public double getIntakeSpeed() { return intakeSpeed; }

    public static class Builder extends BaseSystemConstants.Builder<Builder> {
        private double idleSpeed = 0.0;
        private double intakeSpeed = 1.0;

        public Builder idleSpeed(double speed) {
            this.idleSpeed = speed;
            return this;
        }
        public Builder intakeSpeed(double speed) {
            this.intakeSpeed = speed;
            return this;
        }
        @Override
        public RollerSystemConstants build() {
            return new RollerSystemConstants(this);
        }
    }
}
