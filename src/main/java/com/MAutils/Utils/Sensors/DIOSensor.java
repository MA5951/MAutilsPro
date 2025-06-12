
package com.MAutils.Utils.Sensors;


import com.MAutils.Utils.Sensors.SensorTypes.SensorType;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Robot;

public class DIOSensor extends BaseSensor<Boolean> {

    private DigitalInput dioSensor;
    private boolean value;
    private boolean invert;
    private String name;

    public DIOSensor(String name,int id , boolean invert) {
        dioSensor = new DigitalInput(id);
        this.invert = invert;
        this.name = name;

    }

    @Override
    public double get() {
        if (Robot.isSimulation()) {
            return value ? 1 : 0;
        }
        return (invert ? !dioSensor.get() : dioSensor.get()) ? 1 : 0;
    }

    @Override
    public void set(Boolean value) {
        this.value = (Boolean)value;
    }

    public SensorType getType() {
        return SensorType.BOOLEAN;
    }

    @Override
    public String getName() {
        return name;
    }

}