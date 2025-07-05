
package com.MAutils.Simulation;


import static edu.wpi.first.units.Units.Amps;

import org.ironmaple.simulation.motorsims.SimulatedBattery;

import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.PowerSystemIO;
import com.MAutils.Utils.Constants;
import com.MAutils.Utils.ConvUtil;

import edu.wpi.first.units.measure.Current;
import edu.wpi.first.wpilibj.Notifier;

public class SimulatedSubsystem<T extends PowerSystemIO> {

    @SuppressWarnings("resource")
    public static <T extends PowerSystemIO> T createSimulatedSubsystem(T subsystem) {//Add current Invert?
        SimulatedBattery.addElectricalAppliances(() -> Current.ofBaseUnits(subsystem.getCurrent(), Amps));

        Notifier simulationNotifier = new Notifier(
                () -> {
                    subsystem.getSystemConstants().masterSimState
                            .setSupplyVoltage(SimulatedBattery.getBatteryVoltage());

                    subsystem.getSystemConstants().motorSim
                            .setInputVoltage(subsystem.getSystemConstants().masterSimState.getMotorVoltage());
                    subsystem.getSystemConstants().motorSim.update(Constants.LOOP_TIME);

                    subsystem.getSystemConstants().masterSimState
                            .setRawRotorPosition(subsystem.getSystemConstants().motorSim.getAngularPositionRotations()
                                    * subsystem.getSystemConstants().GEAR);
                    subsystem.getSystemConstants().masterSimState
                            .setRotorVelocity(ConvUtil.RPMtoRPS(subsystem.getSystemConstants().motorSim.getAngularVelocityRPM())
                                    * subsystem.getSystemConstants().GEAR);

                });

        simulationNotifier.setName(subsystem.getSystemConstants().systemName + " Subsystem Notifier");
        simulationNotifier.startPeriodic(Constants.LOOP_TIME);

        return subsystem;
    }

}
 