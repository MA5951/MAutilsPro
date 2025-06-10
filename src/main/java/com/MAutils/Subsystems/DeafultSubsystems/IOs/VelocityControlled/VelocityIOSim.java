
package com.MAutils.Subsystems.DeafultSubsystems.IOs.VelocityControlled;

import com.MAutils.Simulation.TalonFXMotorSim;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.VelocitySystemConstants;

import edu.wpi.first.math.system.plant.DCMotor;

public class VelocityIOSim extends VelocityIOReal {

    private TalonFXMotorSim motorSim;

    public VelocityIOSim(VelocitySystemConstants systemConstants) {
        super(systemConstants);
        motorConfig.MotorOutput.Inverted = systemConstants.MOTORS[0].invert;
        motorSim = new TalonFXMotorSim(systemConstants.MOTORS[0].motorController, motorConfig, DCMotor.getKrakenX60(1),
                systemConstants.INERTIA, false);

    }

    @Override
    public void updatePeriodic() {
        super.updatePeriodic();
        motorSim.updateSim();
    }

}
