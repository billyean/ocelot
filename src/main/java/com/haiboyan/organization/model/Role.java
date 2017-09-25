package com.haiboyan.organization.model;

public enum Role {
    CEO(1, "CEO"),
    VP(2, "Vice President"),
    Director(3, "Director"),
    Manager(4, "Manager"),
    Permanent_Employee(5, "Permanent Employee"),
    Contractor(5, "Contractor Employee");

    private final int level;

    private final String name;

    Role(int level, String name) {
        this.level = level;
        this.name = name;
    }

    int getLevel() {
        return level;
    }

    Role nextLevel() {
        switch (this) {
            case VP:
                return CEO;
            case Director:
                return VP;
            case Manager:
                return Director;
            case Permanent_Employee:
                return Manager;
            default:
                return this;
        }
    }
}
