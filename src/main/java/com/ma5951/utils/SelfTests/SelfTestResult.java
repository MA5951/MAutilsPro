package com.ma5951.utils.SelfTests;

public class SelfTestResult {
    public enum Status {
        OK, WARNING, ERROR
    }

    private final Status status;
    private final String message;

    public SelfTestResult(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
