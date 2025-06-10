
package com.MAutils.Subsystems.DeafultSubsystems.IOs.PositionControlled;

import com.MAutils.Simulation.TalonFXMotorSim;
import com.MAutils.Subsystems.DeafultSubsystems.Constants.PositionSystemConstants;

import edu.wpi.first.math.system.plant.DCMotor;

public class PositionIOSim extends PositionIOReal {

    private TalonFXMotorSim motorSim;

    public PositionIOSim(PositionSystemConstants systemConstants) {
        super(systemConstants);
        motorConfig.MotorOutput.Inverted = systemConstants.MOTORS[0].invert;
        motorSim = new TalonFXMotorSim(systemConstants.MOTORS[0].motorController , motorConfig , DCMotor.getKrakenX60(1) , systemConstants.INERTIA , false);
        motorSim.getMotorSimState().setRawRotorPosition((systemConstants.START_POSE / systemConstants.POSITION_FACTOR) * systemConstants.GEAR);

    }

    @Override
    public void updatePeriodic() {
        super.updatePeriodic();
        motorSim.updateSim();
    }

}
