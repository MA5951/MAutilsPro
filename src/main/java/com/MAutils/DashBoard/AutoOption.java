
package com.MAutils.DashBoard;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;


//TODO this supporter what we take about the auto? a adaptiv auto? that easy to use? 
/* TODO we should look at the auto code from the offseason, 25 seson and see if we can write a lib for it like a pre made command and so on 
    we can give it to the new ones 
*/
public class AutoOption {
    private Command autoCommand;
    private String name;
    private boolean isPathPlanner = false;
    private String pathPlannerName;
    private Pose2d startPose;

    public AutoOption(String optionName , String PathPlannerAutoName , Pose2d StartingPose) {
        autoCommand = AutoBuilder.buildAuto(PathPlannerAutoName);
        name = optionName;
        pathPlannerName = PathPlannerAutoName;
        isPathPlanner = true;
        startPose = StartingPose;
    }

    public AutoOption(String optionName,Command command, Pose2d StartingPose) {
        autoCommand = command;
        name = optionName;
        startPose = StartingPose;
    }

    public String getName() {
        return name;
    }

    public boolean isPathPlannerAuto() {
        return isPathPlanner;
    }

    public String getPathPlannerAutoName() {
        return pathPlannerName;
    }

    public Command getCommand() {
        return autoCommand;
    }

    public Pose2d getStartPose() {
        return startPose;
    }
}
