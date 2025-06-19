
package frc.robot.Commands.Swerve;

import com.MAutils.Swerve.SwerveSystemController;
import com.MAutils.Swerve.Controllers.AngleAdjustController;
import com.MAutils.Swerve.Controllers.FieldCentricDrive;
import com.MAutils.Swerve.Utils.SwerveState;

import edu.wpi.first.math.controller.PIDController;
import frc.robot.RobotContainer;
import frc.robot.Subsystems.Swerve.Swerve;
import frc.robot.Subsystems.Swerve.SwerveConstants;

public class SwerveTeleopController extends SwerveSystemController{

    //Init Controllers
    private final FieldCentricDrive fieldCentricDrive;
    private final AngleAdjustController angleAdjustController;

    //Init PIDControllers
    private final PIDController angleAdjustPID = new PIDController(0.1,0,0);

    //Init States
    private final SwerveState TRAVEL_STATE = new SwerveState("Travel");

    public SwerveTeleopController() {
        super(Swerve.getInstance(), SwerveConstants.SWERVE_CONSTANTS ,RobotContainer.getDriverController());

        fieldCentricDrive = new FieldCentricDrive(drivController, swerveSystem ,constants);
        angleAdjustController = new AngleAdjustController(new PIDController(0, 0, 0), 
        () -> swerveSystem.getGyroData().yaw);
    }

    public void ConfigControllers() {
        //Config Controllers
        fieldCentricDrive.withReduction(() -> drivController.getR2() , 0.4);
        fieldCentricDrive.withSclers(1, 0.7);

        angleAdjustPID.enableContinuousInput(180, -180);
        angleAdjustController.withPIDController(angleAdjustPID);
        angleAdjustController.withSetPoint(180);

        //Config States 
        TRAVEL_STATE.withXY(fieldCentricDrive).withOmega(angleAdjustController);
    }

    public void SetSwerveState() {
        setState(TRAVEL_STATE);
    }
}
