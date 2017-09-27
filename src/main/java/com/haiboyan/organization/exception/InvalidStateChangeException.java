package com.haiboyan.organization.exception;

import com.haiboyan.organization.model.State;

public class InvalidStateChangeException extends Exception {
    public InvalidStateChangeException(State preState, State newState) {
        super(String.format("Invalid state change from %s to %s",
                preState.getDescription(), newState.getDescription()));
    }
}
