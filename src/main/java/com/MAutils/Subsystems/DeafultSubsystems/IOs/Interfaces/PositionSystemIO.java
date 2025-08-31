
package com.MAutils.Subsystems.DeafultSubsystems.IOs.Interfaces;

import com.MAutils.Subsystems.DeafultSubsystems.Constants.PositionSystemConstants;

public interface PositionSystemIO extends PowerSystemIO {

    double getSetPoint();

    double getError();

    boolean atPoint();

    void setSystemConstants(PositionSystemConstants systemConstants);

    void setPosition(double position);

    void setPosition(double position, double voltageFeedForward);

    void setPID(double kP, double kI, double kD);

}
