package com.project.p3project.exception;

public class BlankDataException extends RuntimeException {
    public BlankDataException(String message) {
        super(message + " can not be blank");
    }
}
