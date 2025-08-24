
package com.MAutils.Simulation.Simulatables;

import java.util.ArrayList;
import java.util.List;

import com.MAutils.Components.BooleanSensorWrapper;
import com.MAutils.Simulation.Utils.Simulatable;
import com.MAutils.Subsystems.DeafultSubsystems.Systems.PowerControlledSystem;
import com.MAutils.Utils.MultipleRangeMap;
import com.MAutils.Utils.RangeMap;

//All distance units are in meters
public class RobotGamePieceSimulation implements Simulatable {
    private final GamePiece blankGamePiece;
    private ArrayList<GamePiece> gamePieces = new ArrayList<>();
    private RangeMap<Double, ControlSystem> controlSystems = new RangeMap<>();
    private MultipleRangeMap<Double, Sensor> sensors = new MultipleRangeMap<>();

    public class GamePiece {
        public final String type;
        public final double width;
        public double position;

        public GamePiece(String type, double width, double initialPosition) {
            this.type = type;
            this.width = width;
            position = initialPosition;
        }

        public ControlSystem getControllingSystem(RangeMap<Double, ControlSystem> controlSystem) {
            return controlSystem.get(position);
        }

        public List<Sensor> getActivatedSensors(MultipleRangeMap<Double, Sensor> sensors) {
            return sensors.getRangeOverlapping(position - (width / 2), position + (width / 2));
        }
    }

    public class Sensor {
        public final double sensorPosition;
        public final double sensingWidth;
        public final BooleanSensorWrapper sensorWrapper;

        public Sensor(double sensorPosition, double sensingWidth, BooleanSensorWrapper sensorWrapper) {
            this.sensorPosition = sensorPosition;
            this.sensingWidth = sensingWidth;
            this.sensorWrapper = sensorWrapper;
        }

        public Sensor(double sensorPosition, BooleanSensorWrapper sensorWrapper) {
            this(sensorPosition, 0.005, sensorWrapper);
        }
    }

    public class ControlSystem {
        public final double startControlPosition;
        public final double endControlPosition;
        private final PowerControlledSystem system;

        public ControlSystem(double startControlPosition, double endControlPosition, PowerControlledSystem system) {
            this.startControlPosition = startControlPosition;
            this.endControlPosition = endControlPosition;
            this.system = system;
        }

        public PowerControlledSystem getSystem() {
            return system;
        }
    }

    public RobotGamePieceSimulation(GamePiece blankGamePiece) {
        this.blankGamePiece = blankGamePiece;
    }

    public RobotGamePieceSimulation addSystem(ControlSystem system) {
        controlSystems.put(system.startControlPosition, system.endControlPosition, system);
        return this;
    }

    public RobotGamePieceSimulation addSensor(Sensor sensor) {
        sensors.put(sensor.sensorPosition - (sensor.sensingWidth / 2),
                sensor.sensorPosition + (sensor.sensingWidth / 2), sensor);
        return this;
    }

    public void gamePieceEntered() {
        gamePieces.add(new GamePiece(blankGamePiece.type, blankGamePiece.width, 0));
    }

    public void update() {
        for (GamePiece gamePiece : gamePieces) {
            gamePiece.position += (((2 * Math.PI * 1) / 60) * gamePiece.getControllingSystem(controlSystems).getSystem().getVelocity()) * 0.02; //Replace "1" with wheel radiuse constant after mearge 
            //Convertin system angular speed to linear speed of the gamepiece, the getVelocity methodes return the unit depending on the Velocity Factor of the system, I also had this problem in another part of the code, maybe I should add a getAbsoluteVeclotiy that returns a knowen and constant unit, like RPM so I can use it in those places.

            List<Sensor> activatedSensors = gamePiece.getActivatedSensors(sensors);
            for (Sensor sensor : activatedSensors) {
                sensor.sensorWrapper.setValue(true);
            }

            //Add sensor off 
        }
    }

}
