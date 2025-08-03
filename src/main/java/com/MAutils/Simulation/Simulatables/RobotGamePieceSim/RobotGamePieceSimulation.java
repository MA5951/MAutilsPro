
package com.MAutils.Simulation.Simulatables.RobotGamePieceSim;

import java.util.ArrayList;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import com.MAutils.Logger.MALog;
import com.MAutils.Simulation.Utils.Simulatable;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public abstract class RobotGamePieceSimulation implements Simulatable {

    @SuppressWarnings("unchecked")//TODO remove and cheack
    public static final GamePieceSimulationState NULL_LOCATION = new GamePieceSimulationState("NULL_LOCATION",
            () -> new Pose3d(-1, -1, 0, new Rotation3d()));

    protected GamePieceSimulationState currentLocation;
    protected GamePieceSimulationState lastLocation;

    public RobotGamePieceSimulation() {
        currentLocation = NULL_LOCATION;
        lastLocation = NULL_LOCATION;
    }

    private void setLocation(GamePieceSimulationState location) {
        currentLocation.setSensors(false);
        lastLocation = currentLocation;
        currentLocation = location;
        currentLocation.setSensors(true);
    }

    protected void addLocation(BooleanSupplier setCondition, GamePieceSimulationState location) {
        new Trigger(setCondition).onTrue(new InstantCommand(() -> setLocation(location)));
    }

    public abstract void configureLocations();

    public void updateSimulation() {
        MALog.log("/Simulation/Robot GamePiece/Current Location Pose", currentLocation.getGamePieceLocationSupplier()
                .get());
        MALog.log("/Simulation/Robot GamePices/Current Location Name", currentLocation.name);
        MALog.log("/Simulation/Robot GamePices/Last Location Name", lastLocation.name);
    }

}