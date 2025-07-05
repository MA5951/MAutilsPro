
package frc.robot.Subsystems.Intake;

import com.MAutils.CanBus.CANBusID;
import com.MAutils.Components.Motor;
import com.MAutils.Components.Motor.MotorType;
import com.MAutils.RobotControl.State;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PowerSystemConstants;
import com.ctre.phoenix6.signals.InvertedValue;

import frc.robot.PortMap;

public class IntakeConstants {


    public static final PowerSystemConstants POWER_SYSTEM_CONSTANTS = new PowerSystemConstants("Intake",
        new Motor(new CANBusID(30, PortMap.Canivore), MotorType.KRAKEN, "Intake Motor", InvertedValue.Clockwise_Positive));


    public static final State IDLE = new State("IDLE", Intake.getInstance());
    public static final State FORWARD = new State("FORWARD", Intake.getInstance());
    public static final State REVERSE = new State("REVERSE", Intake.getInstance());

}
