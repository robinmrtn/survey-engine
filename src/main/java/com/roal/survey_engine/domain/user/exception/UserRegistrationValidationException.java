package com.roal.survey_engine.domain.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserRegistrationValidationException extends RuntimeException {
    public UserRegistrationValidationException(String msg) {
        super(msg);
    }
}
