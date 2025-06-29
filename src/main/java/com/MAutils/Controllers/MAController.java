
package com.MAutils.Controllers;

import com.MAutils.Logger.MALog;

public interface MAController {


    public int getPort();

    public boolean getL1();

    public boolean getL2();

    public boolean getR1();

    public boolean getR2();

    public boolean getL3();

    public boolean getR3();

    public boolean getActionsUp();

    public boolean getActionsDown();

    public boolean getActionsLeft();

    public boolean getActionsRight();

    public boolean getDpadUp();

    public boolean getDpadDown();

    public boolean getDpadLeft();

    public boolean getDpadRight();

    public boolean getOptionsLeft();

    public boolean getOptionsRight();

    public boolean getMiddle();

    public double getRightTrigger();//TODO: Add option with deadband and sclar

    public double getLeftTrigger();

    public double getRightX();

    public double getRightY();

    public double getLeftX();

    public double getLeftY();

    public void setRumble(double power);

    default public double withDeadbound(double value) {
        return withDeadbound( value, 0.1);
    }

    default public double withDeadbound(double value, double deadbound) {
        return Math.abs(value) < deadbound ? 0 : value;
    }

    default public void log() {
        MALog.log("/Controllers/" + getPort() + "/L1", getL1());
        MALog.log("/Controllers/" + getPort() + "/L2", getL2());
        MALog.log("/Controllers/" + getPort() + "/R1", getR1());
        MALog.log("/Controllers/" + getPort() + "/R2", getR2());
        MALog.log("/Controllers/" + getPort() + "/L3", getL3());
        MALog.log("/Controllers/" + getPort() + "/R3", getR3());
        MALog.log("/Controllers/" + getPort() + "/Actions/Up", getActionsUp());
        MALog.log("/Controllers/" + getPort() + "/Actions/Down", getActionsDown());
        MALog.log("/Controllers/" + getPort() + "/Actions/Left", getActionsLeft());
        MALog.log("/Controllers/" + getPort() + "/Actions/Right", getActionsRight());
        MALog.log("/Controllers/" + getPort() + "/Dpad/Up", getDpadUp());
        MALog.log("/Controllers/" + getPort() + "/Dpad/Down", getDpadDown());
        MALog.log("/Controllers/" + getPort() + "/Dpad/Left", getDpadLeft());
        MALog.log("/Controllers/" + getPort() + "/Dpad/Right", getDpadRight());
        MALog.log("/Controllers/" + getPort() + "/Options/Left", getOptionsLeft());
        MALog.log("/Controllers/" + getPort() + "/Options/Right", getOptionsRight());
        MALog.log("/Controllers/" + getPort() + "/Middle", getMiddle());
        MALog.log("/Controllers/" + getPort() + "/RightTrigger", getRightTrigger());
        MALog.log("/Controllers/" + getPort() + "/LeftTrigger", getLeftTrigger());
        MALog.log("/Controllers/" + getPort() + "/RightX", getRightX());
        MALog.log("/Controllers/" + getPort() + "/RightY", getRightY());
        MALog.log("/Controllers/" + getPort() + "/LeftX", getLeftX());
        MALog.log("/Controllers/" + getPort() + "/LeftY", getLeftY());
    }

}
