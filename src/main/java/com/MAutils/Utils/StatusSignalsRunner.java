
package com.ma5951.utils.RobotControl.Utils;

import java.util.ArrayList;
import java.util.List;

import com.ctre.phoenix6.StatusSignal;

@SuppressWarnings("rawtypes")
public class StatusSignalsRunner {

    private static List<StatusSignal> statusSignals = new ArrayList<>();

    public static void registerSignals(StatusSignal... signals) {
        for (StatusSignal signal : signals) {
            statusSignals.add(signal);
        }

    }

    public static void updateSignals() {
        for (StatusSignal signal : statusSignals) {
            signal.refresh();
        }
        
    }

}
