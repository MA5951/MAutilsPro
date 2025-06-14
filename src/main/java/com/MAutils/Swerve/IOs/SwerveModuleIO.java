
package com.MAutils.Swerve.IOs;

import edu.wpi.first.math.geometry.Rotation2d;

public class SwerveModuleIO {

    public static class SwerveModuleData {
        public boolean isDriveConnected;
        public double drivePosition;
        public double driveVelocity;
        public double driveCurrent;
        public double driveVolts;

        public boolean isSteerConnected;
        public Rotation2d steerPosition;
        public double steerVelocity;
        public double steerCurrent;
        public double steerVolts;

        public double absoluteSteerPosition;
        public boolean isAbsoluteSteerConnected;

        public double[] odometryTimestamps = new double[] {};
        public double[] odometryDrivePositionsRad = new double[] {};
        public Rotation2d[] odometryTurnPositions = new Rotation2d[] {};
    }

    void updateSwerveModuleData(SwerveModuleData data) {
    }

    void setDriveVoltage(double volts) {
    }

    void setSteerVoltage(double volts) {
    }

    void setDriveVelocity(double metersPerSecond) {
    }

    void setSteerPosition(Rotation2d rotation) {
    }

}
