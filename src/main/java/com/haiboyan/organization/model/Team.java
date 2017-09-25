package com.haiboyan.organization.model;

import java.util.UUID;

public class Team {
    private final UUID id = UUID.randomUUID();

    private String name;

    private Employee manager;
}
