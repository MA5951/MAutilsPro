package com.MAutils.Subsystems;

/**
 * Common configuration for all subsystems.
 */
public class BaseSystemConstants {
    protected final int[] motorPorts;
    protected final boolean[] motorInverted;
    protected final double gearRatio;
    protected final int currentLimit;
    protected final String logPath;

    protected BaseSystemConstants(Builder<?> builder) {
        this.motorPorts = builder.motorPorts;
        this.motorInverted = builder.motorInverted;
        this.gearRatio = builder.gearRatio;
        this.currentLimit = builder.currentLimit;
        this.logPath = builder.logPath;
    }

    @SuppressWarnings("unchecked")
    public static abstract class Builder<T extends Builder<T>> {
        private int[] motorPorts = new int[]{};
        private boolean[] motorInverted = new boolean[]{};
        private double gearRatio = 1.0;
        private int currentLimit = 40;
        private String logPath = "";

        public T motorPorts(int... ports) {
            this.motorPorts = ports;
            return (T) this;
        }
        public T invertPorts(boolean... inverted) {
            this.motorInverted = inverted;
            return (T) this;
        }
        public T gearRatio(double ratio) {
            this.gearRatio = ratio;
            return (T) this;
        }
        public T currentLimit(int limit) {
            this.currentLimit = limit;
            return (T) this;
        }
        public T logPath(String path) {
            this.logPath = path;
            return (T) this;
        }
        public abstract BaseSystemConstants build();
    }
}
