
// package frc.robot.Subsystems.Arm;

// import com.MAutils.Components.Motor;
// import com.MAutils.RobotControl.State;
// import com.MAutils.Subsystems.DeafultSubsystems.Constants.PositionSystemConstants;
// import com.MAutils.Utils.GainConfig;
// import com.ctre.phoenix6.signals.InvertedValue;

// import edu.wpi.first.math.system.plant.DCMotor;
// import frc.robot.PortMap;

// public class ArmConstatnts {

//     public static final GainConfig REAL_GAINS = new GainConfig();

//     public static final PositionSystemConstants ARM_CONSTANTS = new PositionSystemConstants(
//             0, 
//             360, 
//             0, 
//             new Motor(PortMap.Arm.ARM_MOTOR, DCMotor.getKrakenX60(1), "Arm Motor", InvertedValue.Clockwise_Positive) 
//     ).withRealGains(REAL_GAINS)
//      .withTolerance(2)
//      .withMotionMagic(100, 100, 0);

     

//     public static final State IDLE = new State("IDLE");
//     public static final State UP = new State("UP");
//     public static final State DOWEN = new State("DOWEN");
// }
