package com.roal.survey_engine.domain.user.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String roleName) {
        super("Role '" + roleName + "' not found");
    }

}
