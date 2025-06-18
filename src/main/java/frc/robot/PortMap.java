
package frc.robot;

import com.MAutils.Swerve.Utils.SwerveModuleID;
import com.MAutils.Utils.CANBusID;
import com.ctre.phoenix6.CANBus;

public class PortMap {

    public static class Swerve {

        private static final CANBusID LEFT_FRONT_ENCODER = new CANBusID(22, new CANBus("Swerve"));
        private static final CANBusID LEFT_FRONT_DRIVE = new CANBusID(8, new CANBus("Swerve"));
        private static final CANBusID LEFT_FRONT_TURNING = new CANBusID(5, new CANBus("Swerve"));

        private static final CANBusID LEFT_BACK_ENCODER = new CANBusID(21, new CANBus("Swerve"));
        private static final CANBusID LEFT_BACK_DRIVE = new CANBusID(4, new CANBus("Swerve"));
        private static final CANBusID LEFT_BACK_TURNING = new CANBusID(9, new CANBus("Swerve"));

        private static final CANBusID RIGHT_FRONT_ENCODER = new CANBusID(23, new CANBus("Swerve"));
        private static final CANBusID RIGHT_FRONT_DRIVE = new CANBusID(7, new CANBus("Swerve"));
        private static final CANBusID RIGHT_FRONT_TURNING = new CANBusID(6, new CANBus("Swerve"));

        private static final CANBusID RIGHT_BACK_ENCODER = new CANBusID(24, new CANBus("Swerve"));
        private static final CANBusID RIGHT_BACK_DRIVE = new CANBusID(2, new CANBus("Swerve"));
        private static final CANBusID RIGHT_BACK_TURNING = new CANBusID(3, new CANBus("Swerve"));

        public static final CANBusID PIGEON2 = new CANBusID(12, new CANBus("Swerve"));

        public static final SwerveModuleID[] SWERVE_MODULE_IDS = {
            new SwerveModuleID(LEFT_FRONT_ENCODER, LEFT_FRONT_DRIVE, LEFT_FRONT_TURNING),
            new SwerveModuleID(LEFT_BACK_ENCODER, LEFT_BACK_DRIVE, LEFT_BACK_TURNING),
            new SwerveModuleID(RIGHT_FRONT_ENCODER, RIGHT_FRONT_DRIVE, RIGHT_FRONT_TURNING),
            new SwerveModuleID(RIGHT_BACK_ENCODER, RIGHT_BACK_DRIVE, RIGHT_BACK_TURNING)
        };
        
    }

}
