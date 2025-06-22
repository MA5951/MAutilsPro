
package frc.robot.Subsystems.Shooter;

import com.MAutils.Components.Motor;
import com.MAutils.RobotControl.SubsystemState;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.VelocitySystemConstants;
import com.MAutils.Utils.CANBusID;
import com.MAutils.Utils.GainConfig;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.math.system.plant.DCMotor;
import frc.robot.PortMap;

public class ShooterConstants {

    private static final GainConfig simGains = new GainConfig()
            .withKP(1);

    public static final VelocitySystemConstants SHOOTER_CONSTANTS = new VelocitySystemConstants(6000,
            new Motor(new CANBusID(35, PortMap.Rio), DCMotor.getKrakenX60(1), "Main Shooter",
                    InvertedValue.Clockwise_Positive))
            .withSimGains(simGains)
            .withTolerance(100);

    public static final SubsystemState IDLE = new SubsystemState( "IDLE");
    public static final SubsystemState SHOOTING = new SubsystemState("SHOOTING");

}
