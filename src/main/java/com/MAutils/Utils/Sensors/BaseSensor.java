
package com.MAutils.Utils.Sensors;

import com.MAutils.Utils.Sensors.SensorTypes.SensorType;

public abstract class BaseSensor<T> {

    public abstract double get();

    public abstract void set(T value);

    public abstract SensorType getType();

    public abstract String getName();

}
