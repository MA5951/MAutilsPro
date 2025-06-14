
package com.MAutils.Swerve.IOs.SwerveModule;

import static edu.wpi.first.units.Units.Rotation;
import static edu.wpi.first.units.Units.Rotations;

import java.util.Arrays;

import org.ironmaple.simulation.drivesims.SwerveModuleSimulation;
import com.MAutils.Swerve.SwerveConstants;
import com.MAutils.Swerve.Utils.PhoenixUtil;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.units.measure.Angle;

public class SwerveModuleSim extends SwerveModuleTalonFX {

    private final SwerveModuleSimulation simulation;

    public SwerveModuleSim(SwerveConstants constants, int index, SwerveModuleSimulation simulation) {
        super(constants, index);
        turnTalonConfig.Slot0.kP = 70;
        turnTalonConfig.Slot0.kI = 0;
        turnTalonConfig.Slot0.kD = 4.5;
        turnTalonConfig.Slot0.kS = 70;
        turnTalonConfig.Slot0.kV = 1.91;
        turnTalonConfig.Slot0.kA = 0;
        turnTalonConfig.Slot0.StaticFeedforwardSign = StaticFeedforwardSignValue.UseClosedLoopSign;
        turnTalonConfig.Feedback.SensorToMechanismRatio = 16;
        turnTalon.getConfigurator().apply(turnTalonConfig);

        this.simulation = simulation;

        simulation.useDriveMotorController(new PhoenixUtil.TalonFXMotorControllerSim(driveTalon, false));

        simulation.useSteerMotorController(
                new PhoenixUtil.TalonFXMotorControllerWithRemoteCancoderSim(turnTalon, false, cancoder, false,
                        Angle.ofBaseUnits(0, Rotation)));
    }

    public void updateSwerveModuleData(SwerveModuleData moduleData) {
        super.updateSwerveModuleData(moduleData);

        moduleData.odometryDrivePositionsRad = Arrays.stream(simulation.getCachedDriveWheelFinalPositions())
                .mapToDouble(angle -> angle.in(Rotations))
                .toArray();

        moduleData.odometryTurnPositions = simulation.getCachedSteerAbsolutePositions();
    }

}
