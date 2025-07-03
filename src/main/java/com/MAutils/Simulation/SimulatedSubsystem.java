
package com.MAutils.Simulation;


import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.PowerSystemIO;
import com.MAutils.Utils.Constants;
import com.MAutils.Utils.ConvUtil;

import edu.wpi.first.wpilibj.Notifier;

public class SimulatedSubsystem {

    @SuppressWarnings("resource")
    public static <T extends PowerSystemIO> T createSimulatedSubsystem(T subsystem) {//Add current Invert
        Notifier simulationNotifier = new Notifier(
                () -> {
                    subsystem.getSystemConstants().masterSimState
                            .setSupplyVoltage(12);

                    subsystem.getSystemConstants().motorSim
                            .setInputVoltage(subsystem.getSystemConstants().masterSimState.getMotorVoltage());
                        System.out.println(subsystem.getSystemConstants().masterSimState.getMotorVoltage());
                    subsystem.getSystemConstants().motorSim.update(0.020);

                    subsystem.getSystemConstants().masterSimState
                            .setRawRotorPosition(subsystem.getSystemConstants().motorSim.getAngularPositionRotations()
                                    * subsystem.getSystemConstants().GEAR);
                    subsystem.getSystemConstants().masterSimState
                            .setRotorVelocity(ConvUtil.RPMtoRPS(subsystem.getSystemConstants().motorSim.getAngularVelocityRPM())
                                    * subsystem.getSystemConstants().GEAR);
                });

        simulationNotifier.setName("Subsystem Notifier");
        simulationNotifier.startPeriodic(Constants.LOOP_TIME);


        return subsystem;
    }

}
