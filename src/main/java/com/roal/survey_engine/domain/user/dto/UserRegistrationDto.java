package com.roal.survey_engine.domain.user.dto;

import java.util.Set;

public record UserRegistrationDto(String username, String password, Set<String> roles) {
}
