
package com.MAutils.RobotControl;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rectangle2d;

public class DeafultSuperStructure {

    protected Supplier<Pose2d> robotPoseSupplier;
    protected Supplier<Double> robotVelocitySupplier;

    protected DeafultSuperStructure(Supplier<Pose2d> robotPoseSupplier, Supplier<Double> robotVelocitySupplier) {
        this.robotPoseSupplier = robotPoseSupplier;
        this.robotVelocitySupplier = robotVelocitySupplier;
    }

    public boolean isRobotIn(Rectangle2d area) {
        return area.contains(robotPoseSupplier.get().getTranslation());
    }

    public boolean isAtPose(Pose2d target, double toleranceMeters) {
        return robotPoseSupplier.get().getTranslation().getDistance(target.getTranslation()) < toleranceMeters;
    }

    public boolean isMoving() {
        return robotVelocitySupplier.get() > 0.02; 
    }

    public boolean hasGamePiece() {
        System.out.println("hasGamePiece() is not implemented");
        return false;
    }

}
