
package com.MAutils.Simulation.Simulatables.RobotGamePieceSim;

import java.util.function.Supplier;

import com.MAutils.Components.SensorWrapper;

import edu.wpi.first.math.geometry.Pose3d;

public class GamePieceSimulationState {

    public final String name;
    private final Supplier<Pose3d> gamePieceLocationSupplier;
    private SensorWrapper<Boolean>[] activeSensors;

    public GamePieceSimulationState(String name, Supplier<Pose3d> gamePieceLocationSupplier, SensorWrapper<Boolean>... activeSensors) {
        this.name = name;
        this.gamePieceLocationSupplier = gamePieceLocationSupplier;
        if (activeSensors != null) {
            this.activeSensors = activeSensors;  
        }
    }

    public Supplier<Pose3d> getGamePieceLocationSupplier() {
        return gamePieceLocationSupplier;
    }

    public void setSensors(boolean value) {
        if (activeSensors != null) {
            for (SensorWrapper<Boolean> sensor : activeSensors) {
                sensor.setValue(value);
            }
        }
    }

}
