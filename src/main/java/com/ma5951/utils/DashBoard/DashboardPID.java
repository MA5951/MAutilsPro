
package com.ma5951.utils.DashBoard;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

public class DashboardPID implements Sendable {

    public DashboardPID() {

    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("PIDController");
        builder.addDoubleProperty("p", () -> 1, null);
        builder.addDoubleProperty("i", () -> 2, null);
        builder.addDoubleProperty("d", () -> 3, null);
    }
}
