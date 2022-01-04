package com.roal.survey_engine.domain.response.exception;

public class InvalidDataFormatException extends RuntimeException {
    public InvalidDataFormatException() {
        super("Invalid Data Format");
    }
}
