package com.roal.survey_engine.domain.user;

public enum UserAuthority {
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER");

    private final String role;

    UserAuthority(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
