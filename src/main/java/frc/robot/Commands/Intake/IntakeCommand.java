
// package frc.robot.Commands.Intake;

// import com.MAutils.RobotControl.SubsystemCommand;

// import frc.robot.Subsystems.Intake.Intake;

// public class IntakeCommand extends SubsystemCommand {
//     private static final Intake intake = Intake.getInstance();

//     public IntakeCommand() {
//         super(intake);
//     }

//     @Override
//     public void Automatic() {
//         switch (subsystem.getCurrentState().stateName) {
//             case "IDLE":
//                 intake.setVoltage(10);
//                 break;
//             case "INTAKE":

//                 break;
//         }
//     }

//     @Override
//     public void Manual() {
//     }

//     @Override
//     public void CantMove() {
//         intake.setVoltage(0);
//     }

// }
