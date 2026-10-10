package com.irhsm.Resident.exception;

public class ResidentNotFoundException extends RuntimeException {
    public ResidentNotFoundException(Long id) {
        super("Resident " + id + " not found");
    }
}