
package com.MAutils.Swerve.IOs.SwerveModule;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Rotation;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import static edu.wpi.first.units.Units.Volts;

import java.util.Arrays;

import org.ironmaple.simulation.drivesims.SwerveModuleSimulation;
import org.ironmaple.simulation.motorsims.SimulatedMotorController;

import com.MAutils.Logger.MALog;
import com.MAutils.Swerve.SwerveSystemConstants;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;


import edu.wpi.first.math.util.Units;
import edu.wpi.first.util.datalog.DataLogIterator;
import edu.wpi.first.util.datalog.DataLogReader;
import edu.wpi.first.util.datalog.DataLogRecord;

public class SwerveModuleReplay implements SwerveModuleIO {


    private String name;

    public SwerveModuleReplay(String name) {
        this.name = name;
    }

    public void updateSwerveModuleData(SwerveModuleData moduleData) {


        moduleData.isDriveConnected = true;
        moduleData.drivePosition = MALog.getReplayEntry("/Subsystems/Swerve/Modules/" + name + "/Drive Position").getDouble(0);
        moduleData.driveVelocity = MALog.getReplayEntry("/Subsystems/Swerve/Modules/" + name + "/Drive Velocity").getDouble(0);
        moduleData.driveVolts = MALog.getReplayEntry("/Subsystems/Swerve/Modules/" + name + "/Drive Volts").getDouble(0);
        moduleData.driveCurrent = Math.abs(MALog.getReplayEntry("/Subsystems/Swerve/Modules/" + name + "/Drive Current").getDouble(0));

        moduleData.isSteerConnected = true;
        moduleData.steerPosition = Rotation2d.fromDegrees(MALog.getReplayEntry("/Subsystems/Swerve/Modules/" + name + "/Steer Position").getDouble(0));
        moduleData.steerVelocity = MALog.getReplayEntry("/Subsystems/Swerve/Modules/" + name + "/Steer Velocity").getDouble(0);
        moduleData.steerVolts = 0;
        moduleData.steerCurrent = 0;

        moduleData.isAbsoluteSteerConnected = true;
        moduleData.absoluteSteerPosition = Rotation2d.fromDegrees(0);
    
        moduleData.odometryDrivePositionsRad = new double[] {
            MALog.getReplayEntry("/Subsystems/Swerve/Modules/" + name + "/Drive Position").getDouble(0) / 0.0508
        };
        moduleData.odometryTurnPositions = new Rotation2d[] {
            Rotation2d.fromDegrees(MALog.getReplayEntry("/Subsystems/Swerve/Modules/" + name + "/Absolute Angle").getDouble(0))
        };
    }

    public void setDriveVoltage(double volts) {
    }

    public void setSteerVoltage(double volts) {
    }

    public void setDriveVelocity(double metersPerSecond) {
    }

    public void setSteerPosition(Rotation2d rotation) {
    }

    public void setDrivePID(double kP, double kI, double kD) {
        
    }

    public void setSteerPID(double kP, double kI, double kD) {
        
    }

}
