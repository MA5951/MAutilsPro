
package com.MAutils.Simulation;


import com.MAutils.Utils.ConvUtil;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.sim.ChassisReference;
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
        physicshSim = new DCMotorSim(LinearSystemId.createDCMotorSystem(motorType, Inertia, motorConfig.Feedback.SensorToMechanismRatio), motorType , 0.007 , 0.007);
        if  (motorConfig.MotorOutput.Inverted == InvertedValue.Clockwise_Positive) {
            motorSimState.Orientation = ChassisReference.CounterClockwise_Positive;
        } else {
            motorSimState.Orientation = ChassisReference.Clockwise_Positive;
        }
        
        configuration = motorConfig;

    }

    public TalonFXSimState getMotorSimState() {
        return motorSimState;
    }

    public void updateSim() {
        motorSimState.setSupplyVoltage(12);
        physicshSim.setInputVoltage(motorSimState.getMotorVoltage());
        physicshSim.update(0.02);


        motorSimState.setRawRotorPosition(physicshSim.getAngularPositionRotations() * configuration.Feedback.SensorToMechanismRatio);
        motorSimState.setRotorVelocity(ConvUtil.RPMtoRPS(physicshSim.getAngularVelocityRPM() * configuration.Feedback.SensorToMechanismRatio));
    }



}
