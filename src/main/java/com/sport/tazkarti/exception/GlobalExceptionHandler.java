package com.sport.tazkarti.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateRecord(DuplicateRecordException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Duplicate Data");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("status", "400");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT.value(), // 409
                "Validation Failed",
                errors
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }}
