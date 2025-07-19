
package com.MAutils.Vision.Util;

import edu.wpi.first.math.geometry.Transform3d;

public class VisionUtil {


    public static Double getDistance(Transform3d robotToCamera, double tagHight, double tagPitch) {
        return (tagHight - robotToCamera.getZ()) / Math.tan(Math.toRadians(robotToCamera.getRotation().getY() + tagPitch));
    }

}
