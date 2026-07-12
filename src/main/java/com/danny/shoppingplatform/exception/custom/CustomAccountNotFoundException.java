package com.danny.shoppingplatform.exception.custom;

public class CustomAccountNotFoundException extends RuntimeException {
    public CustomAccountNotFoundException(String message) {
        super(message);
    }
}
