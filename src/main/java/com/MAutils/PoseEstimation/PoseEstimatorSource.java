
package com.MAutils.PoseEstimation;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.wpilibj.Timer;

public class PoseEstimatorSource {

    private Supplier<Double> fomSupplier;

    public PoseEstimatorSource(Supplier<Double> fomSupplier) {
        this.fomSupplier = fomSupplier;
    }

    public void sendDataWiteTimestemp(Twist2d delta, double fpgaTimeStemp) {
        FOMPoseEstimator.addTwistMeasurement(delta, fomSupplier.get(), fpgaTimeStemp);
    }

    public void sendDataWiteTimestemp(Twist2d delta, double fom ,double fpgaTimeStemp) {
        FOMPoseEstimator.addTwistMeasurement(delta, fom, fpgaTimeStemp);
    }

    public void sendDataLatency(Twist2d delta, double latencyFromFpga) {
        FOMPoseEstimator.addTwistMeasurement(delta, fomSupplier.get(), Timer.getFPGATimestamp() - latencyFromFpga);
    }

    public void sendDataLatency(Twist2d delta, double fom ,double latencyFromFpga) {
        FOMPoseEstimator.addTwistMeasurement(delta, fom, Timer.getFPGATimestamp() - latencyFromFpga);
    }

}
