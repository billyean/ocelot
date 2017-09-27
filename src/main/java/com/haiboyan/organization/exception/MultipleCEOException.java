package com.haiboyan.organization.exception;

public class MultipleCEOException extends Exception {
    public MultipleCEOException() {
        super("Can't have two CEOs");
    }
}
