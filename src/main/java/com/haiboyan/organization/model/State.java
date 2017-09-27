package com.haiboyan.organization.model;

public enum State {
    IN_POSITION("In postion"),
    ON_LEAVE("On Leave"),
    QUITTED("Quitted");

    private final String description;

    State(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean valid() {
        return this == IN_POSITION || this == ON_LEAVE;
    }

    public boolean validNewState(State newState) {
        switch (newState) {
            case IN_POSITION:
                return ON_LEAVE == this;
            case ON_LEAVE:
                return IN_POSITION == this;
            case QUITTED:
                return IN_POSITION == this;
            default:
                return false;
        }
    }
}
