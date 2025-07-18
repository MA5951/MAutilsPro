
package com.MAutils.Simulation.Utils;


public interface Simulatable {
    

    default void simulationInit() {} //TODO should be not a default func

    default void updateSimulation() {} //TODO should be not a default func

    default void autoInit() {} //this func is only for the field game sim?

}
