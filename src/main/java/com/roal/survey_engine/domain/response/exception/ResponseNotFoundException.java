package com.roal.survey_engine.domain.response.exception;

public class ResponseNotFoundException extends RuntimeException {
    public ResponseNotFoundException(long id) {
        super("Response with id " + id + " not found");
    }
}
