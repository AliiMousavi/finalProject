package com.example.phase2.exception;

public class ActivationTokenExpiredException extends RuntimeException{
    public ActivationTokenExpiredException(String message) {
        super(message);
    }
}
