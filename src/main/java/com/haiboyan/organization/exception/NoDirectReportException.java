package com.haiboyan.organization.exception;

public class NoDirectReportException extends Exception {
    public NoDirectReportException() {
        super("No permanent employee report to this employee");
    }
}
