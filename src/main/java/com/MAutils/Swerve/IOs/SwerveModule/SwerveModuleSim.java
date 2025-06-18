
package com.MAutils.Swerve.IOs.SwerveModule;

import static edu.wpi.first.units.Units.Radians;

import java.util.Arrays;

import org.ironmaple.simulation.drivesims.SwerveModuleSimulation;
import com.MAutils.Swerve.SwerveSystemConstants;
import com.MAutils.Swerve.Utils.PhoenixUtil;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

public class SwerveModuleSim extends SwerveModuleTalonFX {

    private final SwerveModuleSimulation simulation;

    public SwerveModuleSim(SwerveSystemConstants constants, int index, SwerveModuleSimulation simulation) {
        super(constants, index);
        this.simulation = simulation;

        setUpSimParams();

        simulation.useDriveMotorController(new PhoenixUtil.TalonFXMotorControllerSim(driveTalon));
        simulation.useSteerMotorController(
                new PhoenixUtil.TalonFXMotorControllerWithRemoteCancoderSim(turnTalon, cancoder));
    }

    private void setUpSimParams() {
        turnTalonConfig.Slot0 = new Slot0Configs()
        .withKP(50)
        .withKI(0)
        .withKD(4.5)
        .withKS(0)
        .withKV(1.91)
        .withKA(0)
        
        .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);
    

        //turnTalonConfig.Feedback.SensorToMechanismRatio = 16;
        turnTalon.getConfigurator().apply(turnTalonConfig);
        

        driveTalonConfig.Slot0 = 
            new Slot0Configs().withKP(0.1).withKI(0).withKD(0).withKS(0).withKV(0.124);
        driveTalon.getConfigurator().apply(driveTalonConfig);
    }

    public void updateSwerveModuleData(SwerveModuleData moduleData) {
        super.updateSwerveModuleData(moduleData);

        moduleData.odometryDrivePositionsRad = Arrays.stream(simulation.getCachedDriveWheelFinalPositions())
                .mapToDouble(angle -> angle.in(Radians))
                .toArray();

        moduleData.odometryTurnPositions = simulation.getCachedSteerAbsolutePositions();
    }

}
