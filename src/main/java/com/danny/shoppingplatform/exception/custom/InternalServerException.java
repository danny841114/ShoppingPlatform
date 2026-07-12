package com.danny.shoppingplatform.exception.custom;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);
    }
}
