package com.example.phase2.exception;

public class NotValidImageException extends RuntimeException{
    public NotValidImageException(String message) {
        super(message);
    }
}
