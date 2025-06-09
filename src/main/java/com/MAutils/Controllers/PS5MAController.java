package com.MAutils.Controllers;

import edu.wpi.first.wpilibj.PS5Controller;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

public class PS5MAController implements MAController {
    private final PS5Controller controller;

    public PS5MAController(int port) {
        this.controller = new PS5Controller(port);
    }

    @Override
    public boolean getL1() {
        return controller.getL1Button();  
    }

    @Override
    public boolean getL2() {
        return controller.getL2Button(); 
    }

    @Override
    public boolean getR1() {
        return controller.getR1Button();  
    }

    @Override
    public boolean getR2() {
        return controller.getR2Button();  
    }

    @Override
    public boolean getL3() {
        return controller.getL3Button();  
    }

    @Override
    public boolean getR3() {
        return controller.getR3Button();  
    }

    @Override
    public boolean getActionsUp() {
        return controller.getTriangleButton(); 
    }

    @Override
    public boolean getActionsDown() {
        return controller.getCrossButton(); 
    }

    @Override
    public boolean getActionsLeft() {
        return controller.getSquareButton();  
    }

    @Override
    public boolean getActionsRight() {
        return controller.getCircleButton();  
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
        return controller.getCreateButton();  
    }

    @Override
    public boolean getOptionsRight() {
        return controller.getOptionsButton(); 
    }

    @Override
    public boolean getMiddle() {
        return controller.getPSButton(); 
    }

    @Override
    public double getRightTrigger() {
        return controller.getR2Axis();
    }

    @Override
    public double getLeftTrigger() {
        return controller.getL2Axis(); 
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
