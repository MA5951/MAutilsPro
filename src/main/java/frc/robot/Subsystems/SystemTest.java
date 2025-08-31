// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems;

import com.MAutils.Components.Motor;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PowerSystemConstants;
import com.ctre.phoenix6.signals.InvertedValue;

import frc.robot.PortMap;

/** Add your docs here. */
public class SystemTest {

    public static final Motor master = new Motor(PortMap.Intake.INTAKE_MOTOR, Motor.MotorType.KRAKEN, "INTAKE_MASTER",
            InvertedValue.Clockwise_Positive);

    public static final Motor slave = new Motor(PortMap.Intake.INTAKE_SLAVE, Motor.MotorType.FALCON, "INTAKE_SLAVE",
            InvertedValue.CounterClockwise_Positive);

    // public static final PowerSystemConstants INTAKE_SYSTEM_CONSTANTS =
    // PowerSystemConstants.builder(systemName, master, motors).

    public static final PowerSystemConstants INTAKE_SYSTEM_CONSTANTS = PowerSystemConstants.builder("INTAKE",
            master, slave)
            .gear(1.0)
            .statorCurrentLimit(true, 60)
            .peakVoltage(12.0 , -12)
            .rampRate(0.1)
            .isBrake(true)
            .positionFactor(1.0)
            .velocityFactor(1.0)
            .inertia(0.01)
            .foc(false)
            .logPath("")
            .build(PowerSystemConstants::new);
}
