
package com.MAutils.Simulation;

import com.MAutils.Subsystems.DeafultSubsystems.Constants.DeafultSystemConstants;
import com.MAutils.Utils.SimStatusSignal;

import edu.wpi.first.wpilibj.simulation.DCMotorSim;

@SuppressWarnings("rawtypes")
public class SimulatedTalonFX {//TODO Still in work

    private final DeafultSystemConstants constants;
    private final DCMotorSim motorSim;

    public SimulatedTalonFX(DeafultSystemConstants systemConstants) {
        constants = systemConstants;
    }

    public SimStatusSignal getVelocity() {

    }




}
