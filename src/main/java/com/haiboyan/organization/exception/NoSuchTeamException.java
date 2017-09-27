package com.haiboyan.organization.exception;

public class NoSuchTeamException extends Exception{
    public NoSuchTeamException(Long teamId) {
        super(String.format("No team found by id %d", teamId));
    }
}
