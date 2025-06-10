
package com.MAutils.Subsystems.DeafultSubsystems.IOs.PowerControlled;

import com.MAutils.Simulation.TalonFXMotorSim;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PowerSystemConstants;

import edu.wpi.first.math.system.plant.DCMotor;

public class PowerIOSim extends PowerIOReal {

    private TalonFXMotorSim motorSim;

    public PowerIOSim(PowerSystemConstants systemConstants) {
        super(systemConstants);
        motorConfig.MotorOutput.Inverted = systemConstants.MOTORS[0].invert;
        motorSim = new TalonFXMotorSim(systemConstants.MOTORS[0].motorController , motorConfig , DCMotor.getKrakenX60(1) , systemConstants.INERTIA , false);

    }

    @Override
    public void updatePeriodic() {
        super.updatePeriodic();
        motorSim.updateSim();
    }

}
