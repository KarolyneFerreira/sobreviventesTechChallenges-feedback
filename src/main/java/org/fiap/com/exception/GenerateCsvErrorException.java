package org.fiap.com.exception;

public class GenerateCsvErrorException extends RuntimeException {
    public GenerateCsvErrorException(String message) {
        super(message);
    }
}