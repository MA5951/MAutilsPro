
package com.MAutils.CanBus;

import com.ctre.phoenix6.BaseStatusSignal;

public class StatusSignalsRunner {

    private static BaseStatusSignal[] canivoreSignals = new BaseStatusSignal[0];

    private static BaseStatusSignal[] rioSignals = new BaseStatusSignal[0];

    public static void registerSignals(boolean canivore, BaseStatusSignal... signals) {
        if (canivore) {
            BaseStatusSignal[] newSignals = new BaseStatusSignal[canivoreSignals.length + signals.length];
            System.arraycopy(canivoreSignals, 0, newSignals, 0, canivoreSignals.length);
            System.arraycopy(signals, 0, newSignals, canivoreSignals.length, signals.length);
            canivoreSignals = newSignals;
        } else {
            BaseStatusSignal[] newSignals = new BaseStatusSignal[rioSignals.length + signals.length];
            System.arraycopy(rioSignals, 0, newSignals, 0, rioSignals.length);
            System.arraycopy(signals, 0, newSignals, rioSignals.length, signals.length);
            rioSignals = newSignals;
        }
    }

    public static void registerSignals(CANBusID canBusID, BaseStatusSignal... signals) {
        if (canBusID.bus.getName() != "rio") {
            BaseStatusSignal[] newSignals = new BaseStatusSignal[canivoreSignals.length + signals.length];
            System.arraycopy(canivoreSignals, 0, newSignals, 0, canivoreSignals.length);
            System.arraycopy(signals, 0, newSignals, canivoreSignals.length, signals.length);
            canivoreSignals = newSignals;
        } else {
            BaseStatusSignal[] newSignals = new BaseStatusSignal[rioSignals.length + signals.length];
            System.arraycopy(rioSignals, 0, newSignals, 0, rioSignals.length);
            System.arraycopy(signals, 0, newSignals, rioSignals.length, signals.length);
            rioSignals = newSignals;
        }
    }

    /** Refresh all registered signals. */
    public static void refreshAll() {
        if (canivoreSignals.length > 0) {
            BaseStatusSignal.refreshAll(canivoreSignals);
        }
        if (rioSignals.length > 0) {
            BaseStatusSignal.refreshAll(rioSignals);
        }
    }

}
