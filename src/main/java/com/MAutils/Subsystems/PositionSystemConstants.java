package com.ma5951.utils.subsystems;

/**
 * Configuration specific to a position-controlled subsystem.
 */
public class PositionSystemConstants extends BaseSystemConstants {
    private final double p;
    private final double i;
    private final double d;
    private final double kF;
    private final double minPosition;
    private final double maxPosition;

    private PositionSystemConstants(Builder builder) {
        super(builder);
        this.p = builder.p;
        this.i = builder.i;
        this.d = builder.d;
        this.kF = builder.kF;
        this.minPosition = builder.minPosition;
        this.maxPosition = builder.maxPosition;
    }

    public double getP() { return p; }
    public double getI() { return i; }
    public double getD() { return d; }
    public double getkF() { return kF; }
    public double getMinPosition() { return minPosition; }
    public double getMaxPosition() { return maxPosition; }

    public static class Builder extends BaseSystemConstants.Builder<Builder> {
        private double p = 0.0;
        private double i = 0.0;
        private double d = 0.0;
        private double kF = 0.0;
        private double minPosition = 0.0;
        private double maxPosition = 0.0;

        public Builder p(double p) { this.p = p; return this; }
        public Builder i(double i) { this.i = i; return this; }
        public Builder d(double d) { this.d = d; return this; }
        public Builder kF(double kF) { this.kF = kF; return this; }
        public Builder minPosition(double min) { this.minPosition = min; return this; }
        public Builder maxPosition(double max) { this.maxPosition = max; return this; }
        @Override
        public PositionSystemConstants build() {
            return new PositionSystemConstants(this);
        }
    }
}
