package com.irhsn.medicine.exception;

// Thrown when a request breaks a rule, e.g. jumping a request
// from PENDING straight to RECEIVED. Will become HTTP 400 later.
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}