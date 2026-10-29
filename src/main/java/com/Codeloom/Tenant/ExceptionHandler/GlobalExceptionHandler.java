package com.Codeloom.Tenant.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> buildError(
            Exception ex,
            HttpStatus status,
            HttpServletRequest request
    ) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, status);
    }

    /// this handle illegal exception
    @ExceptionHandler(IllegalArgumentException.class) // Or your custom ValidationException
    public ResponseEntity<ErrorResponse> handleValidation(IllegalArgumentException ex, HttpServletRequest request) {
        return buildError(ex, HttpStatus.INTERNAL_SERVER_ERROR,request);
    }

    //  Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        return buildError(ex, HttpStatus.NOT_FOUND, req);
    }

    // Handles bad Request
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
        return buildError(ex, HttpStatus.BAD_REQUEST, req);
    }

    //  Unauthorized / JWT / Login Errors
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex, HttpServletRequest req) {
        return buildError(ex, HttpStatus.UNAUTHORIZED, req);
    }


    //  Validate Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Handles default catch-All
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, HttpServletRequest req) {
        return buildError(ex, HttpStatus.INTERNAL_SERVER_ERROR, req);
    }

    //this handles exception if the file to be sent is not available
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> FileNotFoundException(Exception ex, HttpServletRequest req) {
        return buildError(ex,HttpStatus.INTERNAL_SERVER_ERROR,req);
    }

}

