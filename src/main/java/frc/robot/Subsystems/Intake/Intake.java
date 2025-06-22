
package frc.robot.Subsystems.Intake;

import com.MAutils.Subsystems.DeafultSubsystems.Systems.PowerControlledSystem;

public class Intake extends PowerControlledSystem{
    private static Intake intake;


    public Intake() {
        super("Intake", IntakeConstants.INTAKE_CONSTANTS, IntakeConstants.IDLE, IntakeConstants.INTAKE);
    }


    @Override
    public boolean CAN_MOVE() {
        return true; // This can be modified based on specific conditions for the intake system
    }

    public static Intake getInstance() {
        if (intake == null) {
            intake = new Intake();
        }
        return intake;
    }
}
