package org.mounanga.backend.exception;

public class BarcodeAlreadyExistException extends RuntimeException{
    public BarcodeAlreadyExistException(String message) {
        super(message);
    }
}
