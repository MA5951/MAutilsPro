
package com.MAutils.Simulation.Simulatables;

import java.util.ArrayList;
import java.util.function.Supplier;

import com.MAutils.Logger.MALog;
import com.MAutils.Simulation.Utils.Simulatable;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;

public class GamePieceStateSimulation implements Simulatable {
    public static final GamePieceState BLANK_STATE = new GamePieceState("BLANK_STATE", () -> false,
            () -> new Pose3d(-1, -1, -1, new Rotation3d()));

    public static class GamePieceState {
        public final Supplier<Pose3d> gamePiecePoseSupplier;
        public final String stateName;
        public final Supplier<Boolean> stateEnterSupplier;

        public GamePieceState(String stateName, Supplier<Boolean> stateEnterSupplier,
                Supplier<Pose3d> gamePiecePoseSupplier) {
            this.stateName = stateName;
            this.gamePiecePoseSupplier = gamePiecePoseSupplier;
            this.stateEnterSupplier = stateEnterSupplier;
        }

    }

    private static ArrayList<GamePieceState> gamePieceStates = new ArrayList<>();
    private static GamePieceState currentState = BLANK_STATE;

    public static void addStates(GamePieceState... states) {
        for (GamePieceState state : states) {
            gamePieceStates.add(state);
        }
    }

    public static GamePieceState getCurrentState() {
        return currentState;
    }

    @Override
    public void updateSimulation() {
        for (GamePieceState state : gamePieceStates) {
            if (state.stateEnterSupplier.get()) {
                currentState = state;
                break;
            }
        }

        MALog.log("/Simulation/GamePiece/Robot/Current State Name/",
                currentState.stateName);
        MALog.log("/Simulation/GamePiece/Robot/Current State Pose/", currentState.gamePiecePoseSupplier.get());
    }

}
