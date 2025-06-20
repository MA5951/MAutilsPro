
package frc.robot;

import com.MAutils.Swerve.Utils.SwerveModuleID;
import com.MAutils.Utils.CANBusID;
import com.MAutils.Utils.DeafultPortMap;

public class PortMap extends DeafultPortMap{

    public static class Swerve {

        private static final CANBusID LEFT_FRONT_ENCODER = new CANBusID(22, Canivore);
        private static final CANBusID LEFT_FRONT_DRIVE = new CANBusID(8, Canivore);
        private static final CANBusID LEFT_FRONT_TURNING = new CANBusID(5, Canivore);

        private static final CANBusID LEFT_BACK_ENCODER = new CANBusID(21, Canivore);
        private static final CANBusID LEFT_BACK_DRIVE = new CANBusID(4, Canivore);
        private static final CANBusID LEFT_BACK_TURNING = new CANBusID(9, Canivore);

        private static final CANBusID RIGHT_FRONT_ENCODER = new CANBusID(23, Canivore);
        private static final CANBusID RIGHT_FRONT_DRIVE = new CANBusID(7, Canivore);
        private static final CANBusID RIGHT_FRONT_TURNING = new CANBusID(6, Canivore);

        private static final CANBusID RIGHT_BACK_ENCODER = new CANBusID(24, Canivore);
        private static final CANBusID RIGHT_BACK_DRIVE = new CANBusID(2, Canivore);
        private static final CANBusID RIGHT_BACK_TURNING = new CANBusID(3, Canivore);

        public static final CANBusID PIGEON2 = new CANBusID(12, Canivore);

        public static final SwerveModuleID[] SWERVE_MODULE_IDS = {
            new SwerveModuleID(LEFT_FRONT_ENCODER, LEFT_FRONT_DRIVE, LEFT_FRONT_TURNING),
            new SwerveModuleID(LEFT_BACK_ENCODER, LEFT_BACK_DRIVE, LEFT_BACK_TURNING),
            new SwerveModuleID(RIGHT_FRONT_ENCODER, RIGHT_FRONT_DRIVE, RIGHT_FRONT_TURNING),
            new SwerveModuleID(RIGHT_BACK_ENCODER, RIGHT_BACK_DRIVE, RIGHT_BACK_TURNING)
        };
        
    }

    public static class Arm {
        public static final CANBusID ARM_MOTOR = new CANBusID(31, Canivore);

    }

}
