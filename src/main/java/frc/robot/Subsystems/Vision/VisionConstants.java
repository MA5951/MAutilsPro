
package frc.robot.Subsystems.Vision;

import com.MAutils.Utils.ConvUtil;
import com.MAutils.Vision.Filters.FiltersConfig;
import com.MAutils.Vision.IOs.AprilTagCamera;
import com.MAutils.Vision.IOs.LimelightIO;

import edu.wpi.first.math.geometry.Rotation3d;
import frc.robot.RobotContainer;

public class VisionConstants {


    public static final AprilTagCamera frontLeftLimelight = new AprilTagCamera(
        new LimelightIO("limelight-frontl", () -> new Rotation3d()), new FiltersConfig(), () -> ConvUtil.DegreesToRadians(RobotContainer.swerve.getGyro().getGyroData().yaw));





}
