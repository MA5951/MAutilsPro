
package com.MAutils.CanBus;


import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;

public class StatusSignalsRunner {

    private static BaseStatusSignal[] canivoreSignals = new BaseStatusSignal[0];
    //TODO add rio signals

    //Add StatusSignalFacotery that registers itself, and canbus ID to know which canbus
   

    public static void registerSignals(@SuppressWarnings("rawtypes") StatusSignal... signals) {
        BaseStatusSignal[] newSignals = new BaseStatusSignal[canivoreSignals.length + signals.length];
      System.arraycopy(canivoreSignals, 0, newSignals, 0, canivoreSignals.length);
      System.arraycopy(signals, 0, newSignals, canivoreSignals.length, signals.length);
      canivoreSignals = newSignals;

    }

    public static void updateSignals() {
        BaseStatusSignal.refreshAll(canivoreSignals);
    }

}
