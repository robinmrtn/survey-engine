package com.roal.survey_engine.domain.survey.exception;

public class SurveyNotFoundException extends RuntimeException {
    public SurveyNotFoundException(String id) {
        super("Survey with id " + id + " not found");
    }

    public SurveyNotFoundException() {
        super("Survey not found");
    }
}
