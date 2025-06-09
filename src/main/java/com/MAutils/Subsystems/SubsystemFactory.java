package com.ma5951.utils.subsystems;

/**
 * Helpers to create real or sim subsystems.
 */
public final class SubsystemFactory {
    private SubsystemFactory() {}

    public static DefaultSubsystem<RollerSystemConstants, RollerIOReal> createRollerReal(RollerSystemConstants constants) {
        return new DefaultSubsystem<>(constants, new RollerIOReal(constants));
    }
    public static DefaultSubsystem<RollerSystemConstants, RollerIOSim> createRollerSim(RollerSystemConstants constants) {
        return new DefaultSubsystem<>(constants, new RollerIOSim(constants));
    }
    public static DefaultSubsystem<PositionSystemConstants, PositionIOReal> createPositionReal(PositionSystemConstants constants) {
        return new DefaultSubsystem<>(constants, new PositionIOReal(constants));
    }
    public static DefaultSubsystem<PositionSystemConstants, PositionIOSim> createPositionSim(PositionSystemConstants constants) {
        return new DefaultSubsystem<>(constants, new PositionIOSim(constants));
    }
}
