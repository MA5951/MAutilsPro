
package com.MAutils.Simulation;


import org.ironmaple.simulation.motorsims.SimulatedBattery;

import com.MAutils.Logger.MALog;
import com.MAutils.Utils.ConvUtil;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.sim.TalonFXSimState;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class TalonFXMotorSim {

    private TalonFXSimState motorSimState;
    private DCMotorSim physicshSim;
    private TalonFXConfiguration configuration;
    

    public TalonFXMotorSim(TalonFX motor , TalonFXConfiguration motorConfig ,DCMotor motorType , double Inertia , boolean isRevers) {
        motorSimState = motor.getSimState();
        physicshSim = new DCMotorSim(LinearSystemId.createDCMotorSystem(motorType.withReduction(motorConfig.Feedback.SensorToMechanismRatio), Inertia,1 ), motorType );

        
        configuration = motorConfig;

    }

    public TalonFXSimState getMotorSimState() {
        return motorSimState;
    }

    public void updateSim() {
        motorSimState.setSupplyVoltage(SimulatedBattery.getBatteryVoltage());
        physicshSim.setInputVoltage(motorSimState.getMotorVoltage());
        physicshSim.update(0.02);

        MALog.log("/Simulation/TalonFXMotorSim/Voltage", motorSimState.getMotorVoltage());

        motorSimState.setRawRotorPosition(physicshSim.getAngularPositionRotations() * configuration.Feedback.SensorToMechanismRatio);
        motorSimState.setRotorVelocity(ConvUtil.RPMtoRPS(physicshSim.getAngularVelocityRPM() * configuration.Feedback.SensorToMechanismRatio));
    }



}
