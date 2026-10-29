package com.Codeloom.Tenant.ExceptionHandler;

public class FileNotFoundException  extends RuntimeException{
    public FileNotFoundException(String message){
        super(message);
    }
}
