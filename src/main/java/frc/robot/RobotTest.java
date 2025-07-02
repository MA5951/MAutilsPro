
package frc.robot;

import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.MAutils.Logger.MALog;
import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.sim.TalonFXSimState;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class RobotTest extends TimedRobot {

    private TalonFX testMotor = new TalonFX(37);
    private TalonFXSimState testMotorSimState = testMotor.getSimState();
    private DCMotorSim testeMotorSim = new DCMotorSim(
        LinearSystemId.createDCMotorSystem(1.0 , 0.05), DCMotor.getKrakenX60(2));
    //Gear 4

    private StatusSignal<AngularVelocity> motorVelocity = testMotor.getVelocity();
    private StatusSignal<Angle> motorPosition = testMotor.getPosition();
    private StatusSignal<Current> motorCurrent = testMotor.getStatorCurrent();
    private StatusSignal<Voltage> motorVoltage = testMotor.getMotorVoltage();

    public RobotTest() {
        MALog.log("/Test/Velocity", motorVelocity.getValueAsDouble());
        MALog.log("/Test/Position", motorPosition.getValueAsDouble());
        MALog.log("/Test/Current", motorCurrent.getValueAsDouble());
        MALog.log("/Test/Voltage", motorVoltage.getValueAsDouble());
    }

    @Override
    public void teleopPeriodic() {
        testMotor.setVoltage(3);

        testMotorSimState.setSupplyVoltage(12);

        testeMotorSim.setInputVoltage(testMotorSimState.getMotorVoltage());
        testeMotorSim.update(0.020);

        testMotorSimState.setRawRotorPosition(testeMotorSim.getAngularPositionRotations() * 4);
        testMotorSimState.setRotorVelocity(testeMotorSim.getAngularVelocity().in(RotationsPerSecond) * 4);


        BaseStatusSignal.refreshAll(
            motorVelocity,
            motorPosition,
            motorCurrent,
            motorVoltage
        );

        MALog.log("/Test/Velocity", motorVelocity.getValueAsDouble());
        MALog.log("/Test/Position", motorPosition.getValueAsDouble());
        MALog.log("/Test/Current", motorCurrent.getValueAsDouble());
        MALog.log("/Test/Voltage", motorVoltage.getValueAsDouble());
        MALog.log("/Test/Current 2", testeMotorSim.getCurrentDrawAmps());
    }

}
