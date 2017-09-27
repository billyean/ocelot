package com.haiboyan.organization.model;

public class Action {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Action() {}

    public Action(String name) {
        setName(name);
    }
}
