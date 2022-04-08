package com.roal.survey_engine.domain.user;

import java.util.Set;

public record UserDto(String id, String username, String password, Set<String> roles) {
}
