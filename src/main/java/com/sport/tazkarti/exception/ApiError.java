package com.sport.tazkarti.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ApiError {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private Map<String, String> validationErrors;

    public ApiError(int status, String message, Map<String, String> validationErrors) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.validationErrors = validationErrors;
    }
    public ApiError(int status, String message) {
        this(status, message, null);
    }
}
