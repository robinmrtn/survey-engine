package com.roal.survey_engine.survey.exception;

public class SurveyNotFoundException extends RuntimeException {
    public SurveyNotFoundException(long id) {
        super("Survey with id " + id + " not found");
    }

    public SurveyNotFoundException() {
        super("Survey not found");
    }
}
