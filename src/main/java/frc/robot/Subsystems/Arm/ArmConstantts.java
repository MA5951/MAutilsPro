
package frc.robot.Subsystems.Arm;

import com.MAutils.Components.Motor;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PositionSystemConstants;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.math.system.plant.DCMotor;


public class ArmConstantts {

    public static final PositionSystemConstants ARM_CONSTANTS = (PositionSystemConstants) new PositionSystemConstants()
    .withPID(1 , 1 , 1 , 1)
    .withPoseLimits(0, 270, 180)
    .withMotors(
                    new Motor(new TalonFX(0), DCMotor.getFalcon500(1), "Left Intake Motor",
                            InvertedValue.Clockwise_Positive),
                    new Motor(new TalonFX(1), DCMotor.getFalcon500(1), "Right Intake Motor",
                            InvertedValue.CounterClockwise_Positive))
            .withGear(5)
            .withIsBrake(true)
            .withLogPath("Intake")
            .withMotorCurrentLimit(40)
            .withStatorCurrentLimit(true, 30)
            .withPeakVoltage(6, -6)
            .withPositionFactor(1)
            .withVelocityFactor(1)
    ;

}
