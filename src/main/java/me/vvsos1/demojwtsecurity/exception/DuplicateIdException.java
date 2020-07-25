package me.vvsos1.demojwtsecurity.exception;

public class DuplicateIdException extends RuntimeException{
    public DuplicateIdException(Throwable cause) {
        super(cause);
    }

    public DuplicateIdException() {
    }
}