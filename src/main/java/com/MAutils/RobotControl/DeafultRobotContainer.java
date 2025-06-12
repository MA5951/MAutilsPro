package com.MAutils.RobotControl;

import java.util.function.BooleanSupplier;

import org.ironmaple.simulation.SimulatedArena;
import org.ironmaple.simulation.drivesims.SwerveDriveSimulation;

import com.MAutils.Controllers.MAController;
import com.MAutils.Logger.MALog;
import com.MAutils.Utils.StatusSignalsRunner;

import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class DeafultRobotContainer {

    protected static RobotState currentRobotState;
    protected static MAController driverController;
    protected static MAController operatorController;
    private static SwerveDriveSimulation swerveDriveSimulation;

    private static String[] gamePiecesList ;

    public static void setDriverController(MAController controller) {
        driverController = controller;

    }

    public void addSystemCommand(SubsystemCommand command) {
        CommandScheduler.getInstance().setDefaultCommand(command.getCommandSubsystem(), command);
    }

    public static MAController getDriverController() {
        return driverController;
    }

    public static void setOperatorController(MAController controller) {
        operatorController = controller;
    }

    public static MAController getOperatorController() {
        return operatorController;
    }

    public static void setSwerveDriveSimulation(SwerveDriveSimulation simulation) {
        swerveDriveSimulation = simulation;
    }

    public static void setGamePiecesList(String[] gamePieces) {
        gamePiecesList = gamePieces;
    }

    public static void setRobotState(RobotState robotState) {
        currentRobotState = robotState;
    }

    public static RobotState getRobotState() {
        return currentRobotState;
    }

    public static StateTrigger T(BooleanSupplier condition, RobotState stateToSet) {
        return StateTrigger.T(condition, stateToSet);
    }

    public static void robotPeriodic() {
        StatusSignalsRunner.updateSignals();
        CommandScheduler.getInstance().run();
    }

    public static void simulationPeriodic() {
        SimulatedArena.getInstance().simulationPeriodic();
        MALog.log("/Simulation/Simulation Pose", swerveDriveSimulation.getSimulatedDriveTrainPose());
        for (String type : gamePiecesList) {
            MALog.log("Simulation/GamePices/" + type, SimulatedArena.getInstance().getGamePiecesArrayByType(type));
        }
    }

    public static void simulationInit(boolean autoGamePices) {
        SimulatedArena.getInstance().clearGamePieces();
        if (autoGamePices) {
            SimulatedArena.getInstance().resetFieldForAuto();
        }
    }


    

}
