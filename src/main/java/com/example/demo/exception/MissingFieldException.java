package com.example.demo.exception;

public class MissingFieldException extends RuntimeException {

    public MissingFieldException() {
        super("MISSING_FIELD");
    }
}
