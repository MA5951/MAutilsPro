
package com.MAutils.PoseEstimation;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.wpilibj.Timer;

public class PoseEstimatorSource {

    private Supplier<Double> fomSupplier;
    private Twist2d delta;
    private double fom;
    private double fpgaTimeStemp;

    public PoseEstimatorSource(Supplier<Double> fomSupplier) {
        this.fomSupplier = fomSupplier;
    }

    public void sendDataWiteTimestemp(Twist2d delta, double fpgaTimeStemp) {
        this.delta = delta;
        fom = fomSupplier.get();
        this.fpgaTimeStemp = fpgaTimeStemp;
    }

    public void sendDataWiteTimestemp(Twist2d delta, double fom ,double fpgaTimeStemp) {
        this.delta = delta;
        this.fom = fom;
        this.fpgaTimeStemp = fpgaTimeStemp;
    }

    public void sendDataLatency(Twist2d delta, double latencyFromFpga) {
        this.delta = delta;
        this.fom = fomSupplier.get(); 
        this.fpgaTimeStemp = Timer.getFPGATimestamp() - latencyFromFpga;
    }

    public void sendDataLatency(Twist2d delta, double fom ,double latencyFromFpga) {
        this.delta = delta;
        this.fom = fom;
        this.fpgaTimeStemp = Timer.getFPGATimestamp() - latencyFromFpga;
    }

    public Twist2d getDelta() {
        return delta;
    }

    public double getFom() {
        return fom;
    }

    public double getFpgaTimeStemp() {
        return fpgaTimeStemp;
    }

}
