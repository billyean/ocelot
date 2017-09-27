package com.haiboyan.organization.exception;

public class NoSuchEmployeeException extends Exception{
    public NoSuchEmployeeException(Long employeeId) {
        super(String.format("No employee found by id %d", employeeId));
    }
}
