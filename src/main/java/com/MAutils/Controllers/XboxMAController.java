package com.MAutils.Controllers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

public class XboxMAController extends MAController {
    private final XboxController controller;
    private static final double TRIGGER_THRESHOLD = 0.03;

    public XboxMAController(int port) {
        this.controller = new XboxController(port);
        deadbound = 0.3;
    }

    @Override
    public int getPort() {
        return controller.getPort();
    }

    @Override
    public boolean getL1() {
        return controller.getLeftBumperButton();  
    }

    @Override
    public boolean getL2() {
        return controller.getLeftTriggerAxis() > TRIGGER_THRESHOLD;  
    }

    @Override
    public boolean getR1() {
        return controller.getRightBumperButton();  
    }

    @Override
    public boolean getR2() {
        return controller.getRightTriggerAxis() > TRIGGER_THRESHOLD;  
    }

    @Override
    public boolean getL3() {
        return controller.getLeftStickButton();  
    }

    @Override
    public boolean getR3() {
        return controller.getRightStickButton();  
    }

    @Override
    public boolean getActionsUp() {
        return controller.getYButton();  
    }

    @Override
    public boolean getActionsDown() {
        return controller.getAButton(); 
    }

    @Override
    public boolean getActionsLeft() {
        return controller.getXButton(); 
    }

    @Override
    public boolean getActionsRight() {
        return controller.getBButton();  
    }

    @Override
    public boolean getDpadUp() {
        return controller.getPOV() == 0; 
    }

    @Override
    public boolean getDpadDown() {
        return controller.getPOV() == 180; 
    }

    @Override
    public boolean getDpadLeft() {
        return controller.getPOV() == 270;  
    }

    @Override
    public boolean getDpadRight() {
        return controller.getPOV() == 90;  
    }

    @Override
    public boolean getOptionsLeft() {
        return controller.getBackButton();  
    }

    @Override
    public boolean getOptionsRight() {
        return controller.getStartButton();  
    }

    @Override
    public boolean getMiddle() {
        return false;
    }

    @Override
    public double getRightTrigger() {
        return controller.getRightTriggerAxis(); 
    }

    @Override
    public double getLeftTrigger() {
        return controller.getLeftTriggerAxis();  
    }

    @Override
    public double getRightX() {
        return controller.getRightX(); 
    }

    @Override
    public double getRightY() {
        return controller.getRightY();  
    }

    @Override
    public double getLeftX() {
        return controller.getLeftX();  
    }

    @Override
    public double getLeftY() {
        return controller.getLeftY();  
    }

    @Override
    public void setRumble(double power) {
        controller.setRumble(RumbleType.kLeftRumble, power);
        controller.setRumble(RumbleType.kRightRumble, power);  
    }
}
