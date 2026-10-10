package com.irhsn.medicine.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

// @RestControllerAdvice = "Spring, watch every REST controller.
// If one of them throws an exception, come here to build the answer."
// basePackages = only for my REST API (controller.api),
// so my HTML pages are not affected.
@RestControllerAdvice(basePackages = "com.irhsn.medicine.controller.api")
public class ApiExceptionHandler {

    // @ExceptionHandler(X.class) = "when exception X happens, run this method".
    // ResponseEntity = lets me choose both the status code and the body.

    // 404: the thing doesn't exist.
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
    }

    // 409: not enough stock.
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Map<String, String>> handleInsufficient(InsufficientStockException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", e.getMessage()));
    }

    // 400: a rule was broken (e.g. illegal status change).
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
    }

    // 400: @Valid found problems in the JSON sent to me.
    // Collect every field error into one message,
    // e.g. "name: must not be blank, reorderLevel: must be greater than or equal to 0"
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", message));
    }

    // 409: the database refused, e.g. a medicine with the same name already exists
    // (name is unique) or a foreign key would break.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDatabaseRule(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "This conflicts with existing data (duplicate or linked record)"));
    }
}