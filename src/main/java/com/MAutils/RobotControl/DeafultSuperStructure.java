
package com.MAutils.RobotControl;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rectangle2d;

public class DeafultSuperStructure {

    protected Supplier<Pose2d> robotPoseSupplier;

    protected DeafultSuperStructure() {
    
    }

    public boolean isRobotIn(Rectangle2d area) {
        return area.contains(robotPoseSupplier.get().getTranslation());
    }

    public boolean hasGamePiece() {
        System.out.println("hasGamePiece() is not implemented");
        return false;
    }

}
