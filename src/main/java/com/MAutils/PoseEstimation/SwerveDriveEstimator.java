
// package com.MAutils.PoseEstimation;

// import com.MAutils.Swerve.SwerveSystem;
// import com.MAutils.Swerve.SwerveSystemConstants;

// import edu.wpi.first.math.geometry.Twist2d;
// import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
// import edu.wpi.first.math.kinematics.SwerveModulePosition;

// public class SwerveDriveEstimator {

//     private SwerveModulePosition[] lastPositions;
//     private SwerveModulePosition[] deltas = new SwerveModulePosition[4];
//     private SwerveSystemConstants swerveConstants;
//     private SwerveDriveKinematics kinematics;
//     private SwerveSystem swerveSystem;

//     public SwerveDriveEstimator(SwerveSystemConstants swerveConstants, SwerveSystem swerveSystem) {
//         kinematics = swerveConstants.kinematics;
//         this.swerveConstants = swerveConstants;
//         this.swerveSystem = swerveSystem;
//         lastPositions = swerveSystem.getCurrentPositions();
//     }

//     public Twist2d getModulesDelta(SwerveModulePosition[] currentPositions) {
//         for (int i = 0; i < currentPositions.length; i++) {
//             deltas[i].distanceMeters = currentPositions[i].distanceMeters - lastPositions[i].distanceMeters;
//             deltas[i].angle = currentPositions[i].angle.minus(lastPositions[i].angle);
//         }

//         lastPositions = currentPositions;

//         return kinematics.toTwist2d(deltas);
//     }

//     public double getFOM() {

//     }
// }
