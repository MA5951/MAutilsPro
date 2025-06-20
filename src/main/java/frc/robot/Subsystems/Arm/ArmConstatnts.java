
package frc.robot.Subsystems.Arm;

import com.MAutils.Components.Motor;
import com.MAutils.RobotControl.SubsystemState;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PositionSystemConstants;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.math.system.plant.DCMotor;

public class ArmConstatnts {

    public static final PositionSystemConstants ARM_CONSTANTS = (PositionSystemConstants) new PositionSystemConstants(
        new Motor(new TalonFX(55, "*"), DCMotor.getKrakenX60(1), "Arm Motor", InvertedValue.Clockwise_Positive)
    ).withPID(20, 0, 0, 1).withPoseLimits(0, 360, 0).withGear(18).withInertia(0.04).withLogPath("Arm")
    
; 


    public static final SubsystemState IDLE = new SubsystemState("IDLE");
    public static final SubsystemState UP = new SubsystemState("UP");
    public static final SubsystemState DOWEN = new SubsystemState("DOWEN");
}
