package com.MAutils.RobotControl;

import java.util.function.Supplier;

import org.ironmaple.simulation.SimulatedArena;

import com.MAutils.Controllers.MAController;
import com.MAutils.Controllers.PS5MAController;
import com.MAutils.Controllers.XboxMAController;
import com.MAutils.DashBoard.AutoOption;
import com.MAutils.DashBoard.AutoSelector;
import com.MAutils.Logger.MALog;
import com.MAutils.Utils.Constants;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class DeafultRobotContainer {

    protected static RobotState currentRobotState;
    protected static RobotState lastRobotState;
    protected static MAController driverController = new XboxMAController(Constants.DRIVER_CONTROLLER_PORT);
    protected static MAController operatorController = new PS5MAController(Constants.OPERATOR_CONTROLLER_PORT);
    private static AutoSelector autoSelector;


    public DeafultRobotContainer() {
        SimulatedArena.getInstance(); //TODO why here? why not in the sim classes
        autoSelector = new AutoSelector();
    }

    public void setRobotPoseSupplier(Supplier<Pose2d> robotPoseSupplier) { //TODO why its a func here? use the poseestimator / put it in the superstructure 
        autoSelector.setRobotPoseSupplier(robotPoseSupplier);
    }

    public void addAutoOption(Boolean preViz, AutoOption... autoOption) {
        autoSelector.setAutoOptions(autoOption, preViz);
    }

    public Command getAutonomousCommand() {
        return autoSelector.getSelectedAuto().getCommand();
    }

    public AutoOption getSelectedAutoOption() {
        return autoSelector.getSelectedAuto();
    }

    public static void setDriverController(MAController controller) {
        driverController = controller;

    }

    public void addSystemCommand(SubsystemCommand command) { //TODO rename to addDefaultStateCommand
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

    public static void setRobotState(RobotState robotState) { //TODO change to protected
        lastRobotState = currentRobotState;
        currentRobotState = robotState;
    }

    public static RobotState getRobotState() {
        return currentRobotState;
    }

    public static RobotState getLastRobotState() {
        return lastRobotState;
    }

    public static Trigger T(StateTrigger trigger) {
        /* //TODO this should not work.
         * //TODO need to rewrite, put the code the in the bulid func here 
         * add a StateTrigger object to this class
         * think again if the StateTrigger class is necessary you can insted write 4 func and it will be 10 time more esey to use 
         */
        return trigger.build(); 
    }

    public static void runSelfTestCommands(StateSubsystem... subsystems) {
        for (StateSubsystem subsystem : subsystems) {
            Command selfTestCommand = subsystem.getSelfTest();
            if (selfTestCommand != null) {
                selfTestCommand.schedule();
            } else {
                MALog.log("/RobotControl/SelfTest/" + subsystem.subsystemName, "No Self Test Command");
            }
        }
    }

}
