package com.ma5951.utils.Controllers;

import edu.wpi.first.wpilibj.PS5Controller;
import edu.wpi.first.wpilibj.PS5Controller.Button;
import edu.wpi.first.wpilibj.event.BooleanEvent;
import edu.wpi.first.wpilibj.event.EventLoop;

public class MAPS5Controller {
    private final PS5Controller controller;

    public MAPS5Controller(int port) {
        controller = new PS5Controller(port);
    }

    public PS5Controller getController() {
        return controller;
    }

    // Base getters
    public double getLeftX() {
        return controller.getLeftX();
    }

    public double getLeftY() {
        return controller.getLeftY();
    }

    public double getRightX() {
        return controller.getRightX();
    }

    public double getRightY() {
        return controller.getRightY();
    }

    // Deadband-aware getters
    public double getLeftX(double deadband) {
        return applyDeadband(getLeftX(), deadband);
    }

    public double getLeftY(double deadband) {
        return applyDeadband(getLeftY(), deadband);
    }

    public double getRightX(double deadband) {
        return applyDeadband(getRightX(), deadband);
    }

    public double getRightY(double deadband) {
        return applyDeadband(getRightY(), deadband);
    }

    // Utility
    private double applyDeadband(double value, double threshold) {
        return Math.abs(value) < threshold ? 0.0 : value;
    }

    public boolean getSquareButton() {
        return controller.getRawButton(Button.kSquare.value);
    }

    public boolean getCrossButton() {
        return controller.getRawButton(Button.kCross.value);
    }

    public boolean getCircleButton() {
        return controller.getRawButton(Button.kCircle.value);
    }

    public boolean getTriangleButton() {
        return controller.getRawButton(Button.kTriangle.value);
    }

    public boolean getL1Button() {
        return controller.getRawButton(Button.kL1.value);
    }

    public boolean getR1Button() {
        return controller.getRawButton(Button.kR1.value);
    }

    public boolean getL2Button() {
        return controller.getRawButton(Button.kL2.value);
    }

    public boolean getR2Button() {
        return controller.getRawButton(Button.kR2.value);
    }

    public boolean getCreateButton() {
        return controller.getRawButton(Button.kCreate.value);
    }

    public boolean getOptionsButton() {
        return controller.getRawButton(Button.kOptions.value);
    }

    public boolean getL3Button() {
        return controller.getRawButton(Button.kL3.value);
    }

    public boolean getR3Button() {
        return controller.getRawButton(Button.kR3.value);
    }

    public boolean getPSButton() {
        return controller.getRawButton(Button.kPS.value);
    }

    public boolean getTouchpadButton() {
        return controller.getRawButton(Button.kTouchpad.value);
    }

    // Stick deadband checks
    public boolean inDeadband(double threshold) {
        return inLeftStickDeadband(threshold) && inRightStickDeadband(threshold);
    }

    public boolean inLeftStickDeadband(double threshold) {
        return Math.abs(getLeftX()) < threshold &&
                Math.abs(getLeftY()) < threshold;
    }

    public boolean inRightStickDeadband(double threshold) {
        return Math.abs(getRightX()) < threshold &&
                Math.abs(getRightY()) < threshold;
    }

}
