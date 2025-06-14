
package com.MAutils.Swerve.IOs.Gyro;

import edu.wpi.first.math.geometry.Rotation2d;

public interface GyroIO {

    public static class GyroData {
        public boolean isConnected; 
        public double yaw; //Degrees
        public double yawVelocity; //Degrees per second
        public double pitch; //Degrees
        public double roll; //Degrees
        public double[] odometryYawTimestamps = new double[] {};
        public Rotation2d[] odometryYawPositions = new Rotation2d[] {};
    }

    void updateGyroData(GyroData data);

    void resetYaw(double yaw);

}