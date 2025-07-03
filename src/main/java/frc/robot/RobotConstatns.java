
package frc.robot;

import com.MAutils.RobotControl.RobotState;

import frc.robot.Subsystems.Intake.IntakeConstants;


public class RobotConstatns {


    public static final RobotState INTAKE_IDLE = new RobotState("INTAKE_IDLE", IntakeConstants.IDLE);
    public static final RobotState INTAKE_FORWARD = new RobotState("INTAKE_FORWARD", IntakeConstants.FORWARD);
    public static final RobotState INTAKE_REVERSE = new RobotState("INTAKE_REVERSE", IntakeConstants.REVERSE);

}
