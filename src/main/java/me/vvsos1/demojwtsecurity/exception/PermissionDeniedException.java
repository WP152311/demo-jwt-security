package me.vvsos1.demojwtsecurity.exception;

import org.springframework.security.core.AuthenticationException;

public class PermissionDeniedException extends AuthenticationException {
    public PermissionDeniedException(String msg) {
        super(msg);
    }
}
