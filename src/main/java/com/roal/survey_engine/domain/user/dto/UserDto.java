package com.roal.survey_engine.domain.user.dto;

import java.util.Set;

public record UserDto(String id, String username, String email, Set<String> roles, boolean isAdmin) {
}
