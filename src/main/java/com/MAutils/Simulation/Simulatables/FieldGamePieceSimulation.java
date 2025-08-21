
package com.MAutils.Simulation.Simulatables;

import org.ironmaple.simulation.SimulatedArena;
import org.ironmaple.simulation.gamepieces.GamePieceOnFieldSimulation;
import org.ironmaple.simulation.gamepieces.GamePieceProjectile;

import com.MAutils.Logger.MALog;
import com.MAutils.Simulation.SimulationManager;
import com.MAutils.Simulation.Utils.Simulatable;

public class FieldGamePieceSimulation implements Simulatable{
    private static FieldGamePieceSimulation instance;

    private String[] gamePieces;
    private boolean resetForAuto;

    private  FieldGamePieceSimulation(boolean resetForAuto, String... gamePieces) {
        this.gamePieces = gamePieces;
        this.resetForAuto = resetForAuto;
        SimulationManager.registerSimulatable(this);
    }

    public void addGamePieceOnField(GamePieceOnFieldSimulation gamePiece) {
        SimulatedArena.getInstance().addGamePiece(gamePiece);
    }

    public void addGamePieceProjectile(GamePieceProjectile gamePiece) {
        SimulatedArena.getInstance().addGamePieceProjectile(gamePiece);
    }

    @Override
    public void autoInit() {
        SimulatedArena.getInstance().clearGamePieces();
        if (resetForAuto) {
            SimulatedArena.getInstance().resetFieldForAuto();
        }
    }

    @Override
    public void simulationInit() {
        autoInit();
    }


    @Override
    public void updateSimulation() {
        for (String type : gamePieces) {
            MALog.log("Simulation/GamePices/" + type, SimulatedArena.getInstance().getGamePiecesArrayByType(type));
        }
    }

    public static FieldGamePieceSimulation getInstance(boolean resetForAuto, String... gamePieces) {
        if (instance == null) {
            instance = new FieldGamePieceSimulation(resetForAuto, gamePieces);
        }
        return instance;
    }

}
