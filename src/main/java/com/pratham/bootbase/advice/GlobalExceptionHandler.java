package com.pratham.bootbase.advice;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.pratham.bootbase.dto.ApiResponse;
import com.pratham.bootbase.exception.ApiException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    //handle exceptions due to business logic
    @ExceptionHandler(ApiException.class)
    ResponseEntity<ApiResponse<?>> handleApiException(ApiException e){
        ApiResponse<?> apiResponse = new ApiResponse<>(e.getMessage(),null);
        return ResponseEntity.status(e.getStatus()).body(apiResponse);
    }



    //1. for type mismatch in json or malformed json
    //2. for validation error on request body
    //3. for validation on path variable or request params
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class
    })
    public ResponseEntity<ApiResponse<?>> handleRequestErrors(Exception e) {

        Map<String, Object> errors = new HashMap<>();

        if (e instanceof HttpMessageNotReadableException ex) {
            // Check if cause is due to unknown field(s)
            if (ex.getCause() instanceof UnrecognizedPropertyException unrecognizedEx) {
                String unknownField = unrecognizedEx.getPropertyName();
                errors.put("error", "Unknown field(s) found: " + unknownField);
            } else {
                errors.put("error", "Malformed or invalid JSON input");
            }
        }

        else if (e instanceof MethodArgumentNotValidException ex) {
            // Extract field-level validation messages
            Map<String, String> fieldErrors = new HashMap<>();
            ex.getBindingResult().getFieldErrors().forEach(error ->
                    fieldErrors.put(error.getField(), error.getDefaultMessage())
            );
            errors.put("validationErrors", fieldErrors);
        }

        else if (e instanceof ConstraintViolationException ex) {
            // Handle validation on path variables or request params
            Map<String, String> violations = new HashMap<>();
            ex.getConstraintViolations().forEach(v ->
                    violations.put(v.getPropertyPath().toString(), v.getMessage())
            );
            errors.put("validationErrors", violations);
        }

        ApiResponse<?> apiResponse = new ApiResponse<>("Validation failed", errors);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrityException(DataIntegrityViolationException e) {
        String message = "Database constraint violated.";

        // Defensive: avoid leaking SQL or table details
        String causeMessage = e.getMostSpecificCause() != null
                ? e.getMostSpecificCause().getMessage()
                : "";

        if (causeMessage.toLowerCase().contains("unique") || causeMessage.toLowerCase().contains("duplicate")) {
            message = "Duplicate entry. A record with the same value already exists.";
        } else if (causeMessage.toLowerCase().contains("foreign key")) {
            message = "Invalid reference. This record is linked to another entity.";
        } else if (causeMessage.toLowerCase().contains("not-null")) {
            message = "A required field was missing.";
        }

        ApiResponse<?> apiResponse = new ApiResponse<>(message, null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
    }


    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse<?>> handleAllExceptions(Exception e){
        ApiResponse<?> apiResponse = new ApiResponse<>(e.getMessage(),null);
        return ResponseEntity.internalServerError().body(apiResponse);
    }
}
