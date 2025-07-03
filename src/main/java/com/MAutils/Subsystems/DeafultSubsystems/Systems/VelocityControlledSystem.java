
// package com.MAutils.Subsystems.DeafultSubsystems.Systems;

// import com.MAutils.CanBus.StatusSignalsRunner;
// import com.MAutils.RobotControl.StateSubsystem;
// import com.MAutils.Subsystems.DeafultSubsystems.Constants.VelocitySystemConstants;
// import com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces.VelocitySystemIO;
// import com.MAutils.Subsystems.DeafultSubsystems.IOs.VelocityControlled.VelocityIOReal;
// import com.ctre.phoenix6.StatusSignal;

// import frc.robot.Robot;

// public abstract class VelocityControlledSystem extends StateSubsystem {

//     protected VelocitySystemIO systemIO;

//     public VelocityControlledSystem(String name, VelocitySystemConstants systemConstants,
//             @SuppressWarnings("rawtypes") StatusSignal... statusSignals) {
//         super(name);
//         if (Robot.isReal()) {
//             systemIO = new VelocityIOReal(name, systemConstants);
//         } else {
//             //systemIO = new VelocityIOSim(name, systemConstants);
//         }

//         StatusSignalsRunner.registerSignals(systemConstants.master.canBusID, statusSignals);
//     }

//     public VelocityControlledSystem(String name, VelocitySystemConstants systemConstants, VelocitySystemIO simIO) {
//         super(name);
//         if (Robot.isReal()) {
//             systemIO = new VelocityIOReal(name, systemConstants);
//         } else {
//             systemIO = simIO;
//         }
//     }

//     public double getAppliedVolts() {
//         return systemIO.getAppliedVolts();
//     }

//     public double getCurrent() {
//         return systemIO.getCurrent();
//     }

//     public double getPosition() {
//         return systemIO.getPosition();
//     }

//     public double getVelocity() {
//         return systemIO.getVelocity();
//     }

//     public void setVoltage(double voltage) {
//         systemIO.setVoltage(voltage);
//     }

//     public void setBrakeMode(boolean isBrake) {
//         systemIO.setBrakeMode(isBrake);
//     }

//     public double getSetPoint() {
//         return systemIO.getSetPoint();
//     }

//     public double getError() {
//         return systemIO.getError();
//     }

//     public boolean atPoint() {
//         return systemIO.atPoint();
//     }

//     public void setVelocity(double velocity) {
//         systemIO.setVelocity(velocity);
//     }

//     @Override
//     public void periodic() {
//         super.periodic();
//         systemIO.updatePeriodic();
//     }

// }
