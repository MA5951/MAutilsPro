
package com.MAutils.Utils;

import java.util.ArrayList;
import java.util.List;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;

@SuppressWarnings("rawtypes")
public class StatusSignalsRunner {

    private static List<StatusSignal> statusSignals = new ArrayList<>();

    public static void registerSignals(StatusSignal... signals) {
        for (StatusSignal signal : signals) {
            statusSignals.add(signal);
            statusSignals.toArray();
        }

    }

    public static void updateSignals() {
        BaseStatusSignal.refreshAll((BaseStatusSignal[]) statusSignals.toArray());

    }

}
