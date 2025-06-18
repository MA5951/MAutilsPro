
package com.MAutils.Swerve.IOs.SwerveModule;

import static edu.wpi.first.units.Units.Radians;

import java.util.Arrays;

import org.ironmaple.simulation.drivesims.SwerveModuleSimulation;
import com.MAutils.Swerve.SwerveSystemConstants;
import com.MAutils.Swerve.Utils.PhoenixUtil;


public class SwerveModuleSim extends SwerveModuleTalonFX {

    private final SwerveModuleSimulation simulation;

    public SwerveModuleSim(SwerveSystemConstants constants, int index, SwerveModuleSimulation simulation) {
        super(constants, index);
        

        this.simulation = simulation;

        simulation.useDriveMotorController(new PhoenixUtil.TalonFXMotorControllerSim(driveTalon ,false));

        simulation.useSteerMotorController(new PhoenixUtil.TalonFXMotorControllerWithRemoteCancoderSim(turnTalon, false, cancoder, false, edu.wpi.first.units.Units.Degrees.of(0)));
    }

    public void updateSwerveModuleData(SwerveModuleData moduleData) {
        super.updateSwerveModuleData(moduleData);

        moduleData.odometryDrivePositionsRad = Arrays.stream(simulation.getCachedDriveWheelFinalPositions())
                .mapToDouble(angle -> angle.in(Radians))
                .toArray();

        moduleData.odometryTurnPositions = simulation.getCachedSteerAbsolutePositions();
    }

}
