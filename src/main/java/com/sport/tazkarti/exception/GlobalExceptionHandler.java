package com.sport.tazkarti.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
