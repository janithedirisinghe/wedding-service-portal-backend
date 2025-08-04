package com.example.WeddingVenderMngSystem.exception;

public class UnauthorizedVendorAccessException extends RuntimeException {
    public UnauthorizedVendorAccessException(String message) {
        super(message);
    }
}
