package com.roal.survey_engine.domain.user.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String id) {
        super("User '" + id + "' not found");
    }
}
