package com.example.phase2.exception;

public class NotValidPasswordException extends RuntimeException{
    public NotValidPasswordException(String message) {
        super(message);
    }
}
