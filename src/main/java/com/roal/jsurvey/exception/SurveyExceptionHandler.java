package com.roal.jsurvey.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SurveyExceptionHandler {

    @ExceptionHandler(SurveyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleSurveyNotFound() {
    }

    @ExceptionHandler(InvalidDataFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleInvalidData() {
    }

}
