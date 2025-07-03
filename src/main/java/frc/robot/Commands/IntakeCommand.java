
package frc.robot.Commands;

import com.MAutils.RobotControl.SubsystemCommand;

import frc.robot.Subsystems.Intake.Intake;

public class IntakeCommand extends SubsystemCommand {
    private static Intake intake = Intake.getInstance();

    public IntakeCommand() {
        super(intake);
    }

    public void Automatic() {
        switch (subsystem.getCurrentState().stateName) {
            case "IDLE":
                intake.setVoltage(0);
                break;
            case "FORWARD":
                intake.setVoltage(1);
                break;
            case "REVERSE":
                intake.setVoltage(-1);
                break;

        }
    }

    public void Manual() {
    }

    public void CantMove() {
    }

}
