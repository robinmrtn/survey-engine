package com.roal.survey_engine.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Object> handleForbidden() {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
