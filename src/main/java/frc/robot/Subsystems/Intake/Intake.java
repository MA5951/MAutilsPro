
// package frc.robot.Subsystems.Intake;

// import com.MAutils.Subsystems.DeafultSubsystems.Systems.PowerControlledSystem;
// import com.MAutils.Subsystems.SelfTests.Test;

// import edu.wpi.first.wpilibj2.command.Command;

// public class Intake extends PowerControlledSystem{
//     private static Intake intake;


//     public Intake() {
//         super("Intake", IntakeConstants.INTAKE_CONSTANTS, IntakeConstants.IDLE, IntakeConstants.INTAKE);
//     }


//     public boolean CAN_MOVE() {
//         return true; // This can be modified based on specific conditions for the intake system
//     }

//     public static Intake getInstance() {
//         if (intake == null) {
//             intake = new Intake();
//         }
//         return intake;
//     }


//     public Command getSelfTest() {
//         return selfSystemTest
//         .addTest(new Test("Velocity Test", () -> getVelocity() > 1000, () -> setVoltage(12), 5d))
//         .addTest(new Test("Slowdown Test", () -> getVelocity() < 10, () -> setVoltage(5), 1d))
//         .createCommand();
//     }
// }
