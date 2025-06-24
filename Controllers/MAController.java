
package com.MAutils.Controllers;

import com.MAutils.Logger.MALog;

public abstract class MAController {

    protected double deadbound = 0.1;

    public abstract int getPort();

    public abstract boolean getL1();
    public abstract boolean getL2();
    public abstract boolean getR1();
    public abstract boolean getR2();
    public abstract boolean getL3();
    public abstract boolean getR3();

    public abstract boolean getActionsUp();
    public abstract boolean getActionsDown();
    public abstract boolean getActionsLeft();
    public abstract boolean getActionsRight();

    public abstract boolean getDpadUp();
    public abstract boolean getDpadDown();
    public abstract boolean getDpadLeft();
    public abstract boolean getDpadRight();

    public abstract boolean getOptionsLeft();
    public abstract boolean getOptionsRight();
    public abstract boolean getMiddle();

    public abstract double getRightTrigger();
    public abstract double getLeftTrigger();

    public abstract double getRightX();
    public abstract double getRightY();
    public abstract double getLeftX();
    public abstract double getLeftY();

    public abstract void setRumble(double power);

    public double withDeadbound(double value) {
        return Math.abs(value) < deadbound ? 0 : value;
    }

    public double withDeadbound(double value, double deadbound) {
        return Math.abs(value) < deadbound ? 0 : value;
    }

    public void log() {
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
