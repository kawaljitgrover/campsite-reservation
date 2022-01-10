package com.upgrade.campsitereservation.errorhandler;

public class CampsiteException extends RuntimeException {
    private final String action;

    public CampsiteException(String action, String message) {
        super(message);
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
