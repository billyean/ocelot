package com.haiboyan.organization.model;

import java.time.LocalDate;
import java.util.UUID;


enum State {
    OFFERED,
    IN_POSITION,
    ON_LEAVE,
    QUITTED
}

public class Employee {
    private final UUID id = UUID.randomUUID();

    private String name;

    private LocalDate hireDate;

    private Role role;

    private Team team;

    private State state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
