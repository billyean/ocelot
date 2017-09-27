package com.haiboyan.organization.exception;

public class NotAuthroziedPromotionException extends Exception {
    public NotAuthroziedPromotionException(String reason) {
        super(String.format("Not ready to be promoted: %s", reason));
    }
}
