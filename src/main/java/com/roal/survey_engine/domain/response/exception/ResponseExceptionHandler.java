package com.roal.survey_engine.domain.response.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResponseExceptionHandler {
    @ExceptionHandler(InvalidDataFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleInvalidData() {
    }

    @ExceptionHandler(ResponseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleResponseNotFound() {
    }
}
