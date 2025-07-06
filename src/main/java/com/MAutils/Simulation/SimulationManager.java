
package com.MAutils.Simulation;

import com.MAutils.Simulation.Utils.Simulatable;

public class SimulationManager {

    private Simulatable[] simulatableList;

    public SimulationManager(Simulatable... simulatableList) {
        this.simulatableList = simulatableList;
    }

    public void updateSimulation() {
        for (Simulatable simulatable : simulatableList) {
            simulatable.updateSimulation();
        }
    }

    public void simulationInit() {
        for (Simulatable simulatable : simulatableList) {
            simulatable.simulationInit();
        }
    }

    public void autoInit() {
        for (Simulatable simulatable : simulatableList) {
            simulatable.autoInit();
        }
    }

}
