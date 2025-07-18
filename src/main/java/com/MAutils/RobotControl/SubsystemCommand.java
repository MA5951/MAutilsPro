
package com.MAutils.RobotControl;

import edu.wpi.first.wpilibj2.command.Command;

public abstract class SubsystemCommand extends Command { //TODO rename to state machine command

    protected StateSubsystem subsystem;

    public SubsystemCommand(StateSubsystem subsystem) {
        super();
        this.subsystem = subsystem;
        addRequirements(subsystem);
    }

    public StateSubsystem getCommandSubsystem() { //TODO the subsystem should return the command not the command the subsystem 
        return subsystem;
    }

    public abstract void Automatic();

    public abstract void Manual();

    public abstract void CantMove();

    public void CanMove() { //TODO delete
        Automatic();
    }

    public void Auto() {
        Automatic();
    }

    public void Test() { //TODO change to abstract or delete

    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if (subsystem.CAN_MOVE()) {
            switch (RobotControlConstants.getRobotMode()) {
                case TELEOP:
                    switch (subsystem.getSystemMode()) {
                        case AUTOMATIC:
                            Automatic();
                            break;

                        default: 
                            Manual();
                            break;
                    }
                    break;
                case AUTONOMOUS:
                    Auto();
                    break;
                case TEST:
                    Test();
                    break;
                default:
                    CantMove();
                    break;
            }
        } else {
            CantMove();
        }

    }

    @Override
    public void end(boolean interrupted) {
        CantMove();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
