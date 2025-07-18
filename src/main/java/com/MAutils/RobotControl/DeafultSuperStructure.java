
package com.MAutils.RobotControl;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rectangle2d;

public abstract class DeafultSuperStructure {
    //TODO what about the field class?

    protected Supplier<Pose2d> robotPoseSupplier;
    protected Supplier<Double> robotVelocitySupplier;

    protected DeafultSuperStructure(Supplier<Pose2d> robotPoseSupplier, Supplier<Double> robotVelocitySupplier) {
        this.robotPoseSupplier = robotPoseSupplier;
        this.robotVelocitySupplier = robotVelocitySupplier;
       
    }

    
    public boolean isRobotIn(Rectangle2d area) { //TODO add the Ellipse2d as a func also
        return area.contains(robotPoseSupplier.get().getTranslation());
    }

    public boolean isAtPose(Pose2d target, double toleranceMeters) {
        return robotPoseSupplier.get().getTranslation().getDistance(target.getTranslation()) < toleranceMeters;
    }

    public boolean isMoving() {
        return robotVelocitySupplier.get() > 0.02; //TODO change to constantns
    }

    //TODO add a return func to robotspeed 
    //TODO add abstract func updateSwerveLimits
    //TODO add vision func like isTage, gettage
    public abstract boolean hasGamePiece();
    
    public abstract void updateSwerveControllers();

    public abstract void update();

}
