
package frc.robot.Subsystems.Vision;

import com.MAutils.Vision.Filters.FiltersConfig;
import com.MAutils.Vision.IOs.AprilTagCamera;
import com.MAutils.Vision.IOs.LimelightIO;

public class VisionConstants {


    public static final AprilTagCamera frontLeftLimelight = new AprilTagCamera(
        new LimelightIO("limelight-frontl"), new FiltersConfig());





}
