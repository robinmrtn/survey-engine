package com.roal.jsurvey.exception;

public class NonExistingSurveyException extends RuntimeException {
    public NonExistingSurveyException() {
        super("Survey not found");
    }


}
