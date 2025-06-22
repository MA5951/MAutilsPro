
package frc.robot.Subsystems.Arm;


import com.MAutils.Subsystems.DeafultSubsystems.Systems.PositionControlledSystem;

import edu.wpi.first.wpilibj2.command.Command;

public class Arm extends PositionControlledSystem{
    private static Arm arm;


    public Arm() {
        super("Arm" ,ArmConstatnts.ARM_CONSTANTS
        , ArmConstatnts.IDLE, ArmConstatnts.UP, ArmConstatnts.DOWEN);
    }


    @Override
    public boolean CAN_MOVE() {
        return true;
    }


    public static Arm getInstance() {
        if (arm == null) {
            arm = new Arm();
        }
        return arm;
    }


    @Override
    public Command getSelfTest() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSelfTest'");
    }

}