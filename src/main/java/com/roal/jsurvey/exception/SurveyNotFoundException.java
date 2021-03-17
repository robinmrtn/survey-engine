package com.roal.jsurvey.exception;

public class SurveyNotFoundException extends RuntimeException {
    public SurveyNotFoundException() {
        super("Survey not found");
    }
}
