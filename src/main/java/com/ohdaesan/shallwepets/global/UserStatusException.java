package com.ohdaesan.shallwepets.global;

import org.springframework.security.core.AuthenticationException;

public class UserStatusException extends AuthenticationException {
    public UserStatusException(String msg) {
        super(msg);
    }
}
