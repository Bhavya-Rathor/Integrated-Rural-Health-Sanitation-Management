package com.irhsm.Resident.exception;

public class VisitNotFoundException extends RuntimeException {
    public VisitNotFoundException(Long id) {
        super("Visit " + id + " not found");
    }
}