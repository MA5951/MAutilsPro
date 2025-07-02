
// package frc.robot.Commands.Shooter;

// import com.MAutils.RobotControl.SubsystemCommand;

// import frc.robot.Subsystems.Shooter.Shooter;

// public class ShooterCommand extends SubsystemCommand{
//     private static Shooter shooter = Shooter.getInstance();

//     public ShooterCommand() {
//         super(shooter);
//     }

//     @Override
//     public void Automatic() {
//         switch (shooter.getCurrentState().stateName) {
//             case "IDLE":
//                 shooter.setVelocity(65000);
//                 break;
//             case "SHOOTING":
//                 shooter.setVelocity(5000); 
//                 break;
//         }
//     }

//     @Override
//     public void Manual() {
//         // Implement manual control logic if needed
//     }

//     @Override
//     public void CantMove() {
//         shooter.setVoltage(0);
//     }




// }
