
package frc.robot.Commands.Arm;

import com.MAutils.RobotControl.SubsystemCommand;

import frc.robot.Subsystems.Arm.Arm;



public class ArmCommand extends SubsystemCommand{
    private static Arm arm = Arm.getInstance();

    public ArmCommand() {
        super(arm);
    }

    @Override
    public void Automatic() {
        switch (arm.getCurrentState().stateName) {
            case "IDLE":
                //arm.setPosition(0);
                arm.setVoltage(10);
                break;
            case "UP":
                arm.setPosition(180);
                break;
            case "DOWEN":   
                arm.setPosition(90);
                break;

        }
    }

    @Override
    public void Manual() {
    }

    @Override
    public void CantMove() {
        arm.setVoltage(0);
    }




}
