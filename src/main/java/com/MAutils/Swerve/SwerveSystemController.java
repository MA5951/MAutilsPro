
package com.MAutils.Swerve;

import com.MAutils.Logger.MALog;
import com.MAutils.Swerve.Controllers.FieldCentricDrive;
import com.MAutils.Swerve.Utils.SwerveState;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;

public abstract class SwerveSystemController extends Command {
  protected SwerveSystem swerveSystem;
  private ChassisSpeeds currentSpeeds;
  
  public FieldCentricDrive fieldCentricDriveController;

  public SwerveSystemController(SwerveSystem swerveSystem, SwerveSystemConstants constants ,XboxController controller) {
    super();
    this.swerveSystem = swerveSystem;
    addRequirements(swerveSystem);
    setName("SwerveSystemController");
    
    fieldCentricDriveController = new FieldCentricDrive(controller,
    () -> controller.getLeftBumperButtonPressed(), 0.4, swerveSystem, constants);

    
  }

  protected void setState(SwerveState state) {
    swerveSystem.setState(state);
  }

  public abstract void ConfigControllers();

  public abstract void SetSwerveState();

  public void initialize() {
    ConfigControllers();
  }

  public void execute() {
    SetSwerveState();


    currentSpeeds = swerveSystem.getState().getSpeeds();
    logCurrentState();


    swerveSystem.drive(currentSpeeds);
  }

  public void end(boolean interrupted) {
    swerveSystem.drive(new ChassisSpeeds(0, 0, 0));
  }

  public boolean isFinished() {
    return false;
  }

  private void logCurrentState() {
    MALog.log("/Subsystems/Swerve/Controllers/SystemController/Current State Speeds", currentSpeeds);
  }
}
