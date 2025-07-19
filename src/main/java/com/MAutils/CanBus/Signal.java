package com.MAutils.CanBus;


import com.ctre.phoenix6.StatusSignal;

public class Signal {

    @SuppressWarnings("rawtypes")
    public static StatusSignal registerSignal(StatusSignal signal, CANBusID canBusID) {
        StatusSignalsRunner.registerSignals(canBusID.bus.getName() != "rio",signal);
        return signal;
    }

}
