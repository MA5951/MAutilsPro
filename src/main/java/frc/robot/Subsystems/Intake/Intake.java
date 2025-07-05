
package frc.robot.Subsystems.Intake;

import com.MAutils.Subsystems.DeafultSubsystems.Systems.PowerControlledSystem;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class Intake extends PowerControlledSystem {
    private static Intake instance;

    private Intake() {
        super(IntakeConstants.POWER_SYSTEM_CONSTANTS);
    }

    @Override
    public Command getSelfTest() {
        return new InstantCommand();
    }

    @Override
    public boolean CAN_MOVE() {
        return true;
    }

    public static Intake getInstance() {
        if (instance == null) {
            instance = new Intake();
        }
        return instance;
    }

}
