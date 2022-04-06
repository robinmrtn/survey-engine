package com.roal.survey_engine.domain.response.exception;

public class ResponseNotFoundException extends RuntimeException {
    public ResponseNotFoundException(String id) {
        super("Response with id " + id + " not found");
    }
}
