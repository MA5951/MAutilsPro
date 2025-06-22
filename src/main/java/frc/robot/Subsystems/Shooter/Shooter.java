
package frc.robot.Subsystems.Shooter;

import com.MAutils.Subsystems.DeafultSubsystems.Systems.VelocityControlledSystem;

public class Shooter extends VelocityControlledSystem{
    private static Shooter shooter;


    public Shooter() {
        super("Shooter", ShooterConstants.SHOOTER_CONSTANTS, ShooterConstants.IDLE, ShooterConstants.SHOOTING);
    }

    @Override
    public boolean CAN_MOVE() {
        return true; // Implement logic to determine if the shooter can move
    }

    public static Shooter getInstance() {
        if (shooter == null) {
            shooter = new Shooter();
        }
        return shooter;
    }
}
