
package com.MAutils.Swerve.IOs;

import static edu.wpi.first.units.Units.DegreesPerSecond;

import org.ironmaple.simulation.drivesims.GyroSimulation;

import com.MAutils.Swerve.SwerveConstants;
import com.MAutils.Utils.PhoenixUtil;

import edu.wpi.first.math.geometry.Rotation2d;

public class GyroSim implements Gyro{

    private GyroSimulation gyroSim;

    public GyroSim(SwerveConstants constants) {
        gyroSim = constants.SWERVE_DRIVE_SIMULATION.getGyroSimulation();
    }

    public void resetYaw(double yaw) {
        gyroSim.setRotation(new Rotation2d(yaw));
    }

    public void updateGyroData(GyroData data) {
        data.isConnected = true;
        data.yaw = gyroSim.getGyroReading().getDegrees();
        data.yawVelocity = gyroSim.getMeasuredAngularVelocity().in(DegreesPerSecond);
        data.pitch = 0;
        data.roll = 0;

        data.odometryYawTimestamps = PhoenixUtil.getSimulationOdometryTimeStamps();
        data.odometryYawPositions = gyroSim.getCachedGyroReadings();
    }

    

}
