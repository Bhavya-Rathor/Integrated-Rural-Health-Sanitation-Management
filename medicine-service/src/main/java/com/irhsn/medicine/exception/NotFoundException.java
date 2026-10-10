package com.irhsn.medicine.exception;

// Thrown when someone asks for something that doesn't exist,
// e.g. medicine number 999. Will become HTTP 404 later.
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}