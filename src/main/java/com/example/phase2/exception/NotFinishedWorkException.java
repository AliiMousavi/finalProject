package com.example.phase2.exception;

public class NotFinishedWorkException extends RuntimeException{
    public NotFinishedWorkException(String message) {
        super(message);
    }
}
