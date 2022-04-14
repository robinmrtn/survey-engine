package com.roal.survey_engine.domain.survey.exception;

public class WorkspaceNotFoundException extends RuntimeException {
    public WorkspaceNotFoundException(String id) {
        super("Workspace '" + id + "' not found");
    }
}
