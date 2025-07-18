
package com.MAutils.Simulation;

import java.util.List;

import com.MAutils.Simulation.Utils.Simulatable;

public class SimulationManager  { 
    //TODO here you can add the swerve
    //TODO where all the other sim code we talk about? the sensor the game piece in robot etc...

    private static List<Simulatable> simulatableList = new java.util.ArrayList<>();

    public static void registerSimulatable(Simulatable simulatable) {
        simulatableList.add(simulatable);
    }

    public static void updateSimulation() {
        for (Simulatable simulatable : simulatableList) {
            simulatable.updateSimulation();
        }
    }

    public static void simulationInit() {
        for (Simulatable simulatable : simulatableList) {
            simulatable.simulationInit();
        }
    }

    public static void autoInit() {
        for (Simulatable simulatable : simulatableList) {
            simulatable.autoInit();
        }
    }

}
