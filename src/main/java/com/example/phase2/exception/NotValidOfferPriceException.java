package com.example.phase2.exception;

public class NotValidOfferPriceException extends RuntimeException{
    public NotValidOfferPriceException(String message) {
        super(message);
    }
}