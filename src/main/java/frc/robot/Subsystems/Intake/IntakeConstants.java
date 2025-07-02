
// package frc.robot.Subsystems.Intake;

// import com.MAutils.CanBus.CANBusID;
// import com.MAutils.Components.Motor;
// import com.MAutils.RobotControl.State;
// import com.MAutils.Subsystems.DeafultSubsystems.Constants.PowerSystemConstants;
// import com.ctre.phoenix6.CANBus;
// import com.ctre.phoenix6.signals.InvertedValue;

// import edu.wpi.first.math.system.plant.DCMotor;

// public class IntakeConstants {



//     public static final PowerSystemConstants<PowerSystemConstants> INTAKE_CONSTANTS = new PowerSystemConstants(
//         new Motor(new CANBusID(28, new CANBus("rio")) , DCMotor.getKrakenX60(1), "Intake Motor", InvertedValue.Clockwise_Positive)
//     ).withGear(6);

//     public static final State IDLE = new State("IDLE");
//     public static final State INTAKE = new State("INTAKE");
// }
