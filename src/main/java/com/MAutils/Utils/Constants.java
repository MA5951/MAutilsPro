
package com.MAutils.Utils;

public class Constants {

    public static final String MAUTILS_VERSION = "2.4.1";

    public enum SimulationType {
        SIM,
        REPLAY;
    }

    public static final double LOOP_TIME = 0.02; // 20ms loop time

    public static final int DRIVER_CONTROLLER_PORT = 0;
    public static final int OPERATOR_CONTROLLER_PORT = 1;

    public static final double EPSILON = 1e-9;


    public static final SimulationType SIMULATION_TYPE = SimulationType.SIM; 

}
