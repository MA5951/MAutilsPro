
package com.ma5951.utils.RobotControl.Utils.Sensors;


public abstract class BaseSensor<T> {

    public abstract double get();

    public abstract void set(T value);

    public abstract String getType();

    public abstract String getName();

}
