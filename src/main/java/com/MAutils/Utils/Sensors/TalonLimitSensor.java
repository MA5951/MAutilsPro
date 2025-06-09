
package com.MAutils.Utils.Sensors;

import com.MAutils.Utils.Motor;
import com.MAutils.Utils.StatusSignalsRunner;
import com.MAutils.Utils.Sensors.SensorTypes.SensorType;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.HardwareLimitSwitchConfigs;
import frc.robot.Robot;

public class TalonLimitSensor extends BaseSensor<Boolean> {

    public enum LimitSwitchSource {
        FORWARD,
        REVERSE
    }

    private boolean value;
    @SuppressWarnings("rawtypes")
    private StatusSignal signal;
    private String name;
    private HardwareLimitSwitchConfigs configuration;

    public TalonLimitSensor(String name,LimitSwitchSource limitSwitchSource, boolean invert, Motor motor) {
        this.name = name;
        configuration.ForwardLimitEnable = false;
        configuration.ReverseLimitEnable = false;
        motor.talonFX.getConfigurator().apply(configuration);

        if (LimitSwitchSource.FORWARD == limitSwitchSource) {
            signal = motor.talonFX.getForwardLimit(false);
        } else if (LimitSwitchSource.REVERSE == limitSwitchSource) {
            signal = motor.talonFX.getReverseLimit(false);
        }

        StatusSignalsRunner.registerSignals(signal);
    }

    @Override
    public double get() {
        if (Robot.isSimulation()) {
            return value ? 1 : 0;
        }
        return signal.getValueAsDouble();
    }

    @Override
    public void set(Boolean value) {
        this.value = value;
    }

    @Override
    public SensorType getType() {
        return SensorType.BOOLEAN;
    }

    @Override
    public String getName() {
        return name;
    }

}
