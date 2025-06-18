
package frc.robot.Subsystems.Intake;

import com.MAutils.Subsystems.DeafultSubsystems.Systems.PowerControlledSystem;

public class Intake extends PowerControlledSystem{
    private static Intake intake;


    public Intake() {
        super("Intake" , IntakeConstants.INTAKE_CONSTANTS);
    }




    public static Intake getInstance() {
        if (intake == null) {
            intake = new Intake();
        }
        return intake;
    }

}