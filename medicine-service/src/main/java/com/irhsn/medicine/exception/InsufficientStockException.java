package com.irhsn.medicine.exception;

// Thrown when someone wants 50 tablets but only 20 are left.
// Will become HTTP 409 (conflict) later.
public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}