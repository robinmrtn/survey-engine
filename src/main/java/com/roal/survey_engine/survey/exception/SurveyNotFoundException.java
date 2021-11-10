package com.roal.survey_engine.survey.exception;

public class SurveyNotFoundException extends RuntimeException {
    public SurveyNotFoundException() {
        super("Survey not found");
    }
}
