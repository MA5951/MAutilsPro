
package com.MAutils.Simulation;

import org.ironmaple.simulation.motorsims.SimulatedBattery;

import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.PowerSystemIO;
import com.MAutils.Utils.Constants;

import edu.wpi.first.wpilibj.Notifier;

public class SimulatedSubsystem {

    @SuppressWarnings("resource")
    public static <T extends PowerSystemIO> T createSimulatedSubsystem(T subsystem) {
        Notifier simulationNotifier = new Notifier(
                () -> {
                    subsystem.getSystemConstants().masterSimState
                            .setSupplyVoltage(SimulatedBattery.getBatteryVoltage());

                    subsystem.getSystemConstants().motorSim
                            .setInputVoltage(subsystem.getSystemConstants().masterSimState.getMotorVoltage());
                    subsystem.getSystemConstants().motorSim.update(0.020);

                    subsystem.getSystemConstants().masterSimState
                            .setRawRotorPosition(subsystem.getSystemConstants().motorSim.getAngularPositionRotations()
                                    * subsystem.getSystemConstants().GEAR);
                    subsystem.getSystemConstants().masterSimState
                            .setRotorVelocity(subsystem.getSystemConstants().motorSim.getAngularVelocityRPM()
                                    * subsystem.getSystemConstants().GEAR);
                });

        simulationNotifier.setName("Subsystem Notifier");
        simulationNotifier.startPeriodic(Constants.LOOP_TIME);


        return subsystem;
    }

}
