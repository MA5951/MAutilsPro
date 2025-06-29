
package frc.robot.Subsystems.Arm;


import com.MAutils.Components.SensorSimWrapper;
import com.MAutils.Subsystems.DeafultSubsystems.Systems.PositionControlledSystem;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;

public class Arm extends PositionControlledSystem{
    private static Arm arm;

    private DigitalInput limitSwitch;
    private SensorSimWrapper<Boolean> limitSwitchSim;

    public Arm() {
        super("Arm" ,ArmConstatnts.ARM_CONSTANTS
        , ArmConstatnts.IDLE, ArmConstatnts.UP, ArmConstatnts.DOWEN);

        limitSwitch = new DigitalInput(7);
        limitSwitchSim = new SensorSimWrapper<Boolean>(() -> limitSwitch.get());
    }

    public void registerSignals() {
        super.register(signal);
    }

    public boolean isLimitSwitchPressed() {
        return limitSwitch.se
    }

    public SensorSimWrapper<Boolean> getLimitSwitchSim() {
        return limitSwitchSim;
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

    @Override
    public void periodic() {
        super.periodic();
        System.out.println(isLimitSwitchPressed());
    }

}