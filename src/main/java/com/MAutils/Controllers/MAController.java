
package com.MAutils.Controllers;


public interface MAController {

    boolean getL1();
    boolean getL2();
    boolean getR1();
    boolean getR2();
    boolean getL3();
    boolean getR3();

    boolean getActionsUp();
    boolean getActionsDown();
    boolean getActionsLeft();
    boolean getActionsRight();

    boolean getDpadUp();
    boolean getDpadDown();
    boolean getDpadLeft();
    boolean getDpadRight();

    boolean getOptionsLeft();
    boolean getOptionsRight();
    boolean getMiddle();

    double getRightTrigger();
    double getLeftTrigger();

    double getRightX();
    double getRightY();
    double getLeftX();
    double getLeftY();

    void setRumble(double power);

    default double withDeadbound(double value, double deadbound) {
        return Math.abs(value) < deadbound ? 0 : value;
    }
    
} 
