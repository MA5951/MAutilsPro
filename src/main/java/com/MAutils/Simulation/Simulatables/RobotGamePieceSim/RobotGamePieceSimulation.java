
package com.MAutils.Simulation.Simulatables.RobotGamePieceSim;

import java.util.function.BooleanSupplier;

import com.MAutils.Logger.MALog;
import com.MAutils.Simulation.Utils.Simulatable;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public abstract class RobotGamePieceSimulation implements Simulatable {

    @SuppressWarnings("unchecked")//TODO remove and cheack
    public static final GamePieceSimulationState OUTSIDE_ROBOT = new GamePieceSimulationState("OUTSIDE_ROBOT",
            () -> new Pose3d(-1, -1, 0, new Rotation3d()));

    protected GamePieceSimulationState currentLocation;
    protected GamePieceSimulationState lastLocation;

    public RobotGamePieceSimulation() {
        currentLocation = OUTSIDE_ROBOT;
        lastLocation = OUTSIDE_ROBOT;
    }

    private void setGamePieceState(GamePieceSimulationState gamePieceState) {
        currentLocation.setSensors(false);
        lastLocation = currentLocation;
        currentLocation = gamePieceState;
        currentLocation.setSensors(true);
    }

    protected void addLocation(BooleanSupplier setCondition, GamePieceSimulationState gamePieceState) {
        new Trigger(setCondition).onTrue(new InstantCommand(() -> setGamePieceState(gamePieceState)));
    }

    public abstract void configureLocations();

    public void updateSimulation() {
        MALog.log("/Simulation/Robot GamePiece/Current State Pose", currentLocation.getGamePieceLocationSupplier()
                .get());
        MALog.log("/Simulation/Robot GamePices/Current State Name", currentLocation.name);
        MALog.log("/Simulation/Robot GamePices/Last State Name", lastLocation.name);
    }

    public GamePieceSimulationState getCurrentState() {
        return currentLocation;
    }

}