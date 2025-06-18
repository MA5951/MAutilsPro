
package com.MAutils.Utils;

import java.util.ArrayList;
import java.util.List;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;

@SuppressWarnings("rawtypes")
public class StatusSignalsRunner {

    private static BaseStatusSignal[] canivoreSignals = new BaseStatusSignal[0];


    public static void registerSignals(StatusSignal... signals) {
        BaseStatusSignal[] newSignals = new BaseStatusSignal[canivoreSignals.length + signals.length];
      System.arraycopy(canivoreSignals, 0, newSignals, 0, canivoreSignals.length);
      System.arraycopy(signals, 0, newSignals, canivoreSignals.length, signals.length);
      canivoreSignals = newSignals;

    }

    public static void updateSignals() {
        BaseStatusSignal.refreshAll(canivoreSignals);
    }

}
