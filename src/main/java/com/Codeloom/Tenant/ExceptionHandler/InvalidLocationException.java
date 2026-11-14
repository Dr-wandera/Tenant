package com.Codeloom.Tenant.ExceptionHandler;

public class InvalidLocationException extends RuntimeException {

    public InvalidLocationException(String message) {
        super(message);
    }
}
