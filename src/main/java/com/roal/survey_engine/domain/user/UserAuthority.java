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

    public static class Constants {
        public static final String ADMIN_VALUE = "ROLE_ADMIN";
        public static final String USER_VALUE = "ROLE_USER";
    }
}
