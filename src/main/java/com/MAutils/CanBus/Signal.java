package com.MAutils.CanBus;


import com.ctre.phoenix6.StatusSignal;

public class Signal { //TODO change the name to signal factory, maybe we should create a cover claa to talonfx 
//TODO if not use delete
    @SuppressWarnings("rawtypes")
    public static StatusSignal registerSignal(StatusSignal signal, CANBusID canBusID) {
        StatusSignalsRunner.registerSignals(canBusID.bus.getName() != "rio",signal);
        return signal;
    }

}
