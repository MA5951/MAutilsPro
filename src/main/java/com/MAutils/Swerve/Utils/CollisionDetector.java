
package com.MAutils.Swerve.Utils;

import java.util.function.Supplier;

import com.MAutils.Logger.MALog;
import com.MAutils.Swerve.IOs.Gyro.GyroIO.GyroData;

import edu.wpi.first.math.geometry.Translation2d;

public class CollisionDetector {

    private final Supplier<GyroData> gyroDataSupplier;
    private double collisionVectorSize = 0;


    public CollisionDetector(Supplier<GyroData> gyroDataSupplierr) {
        this.gyroDataSupplier = gyroDataSupplierr;
    }

    public double getForceVectorSize() {
        collisionVectorSize = Math.sqrt(Math.pow(getCollisionVector().getX(), 2) +
        Math.pow(getCollisionVector().getY(), 2));

        MALog.log("/Subsystems/Swerve/Collision Skid/Collision Vector", collisionVectorSize);
        return collisionVectorSize;
    }

    public Translation2d getCollisionVector() {
        return new Translation2d(gyroDataSupplier.get().accelX, gyroDataSupplier.get().accelY);
    }

}
