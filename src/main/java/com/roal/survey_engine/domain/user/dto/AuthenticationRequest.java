package com.roal.survey_engine.domain.user.dto;

import javax.validation.constraints.NotBlank;

public record AuthenticationRequest(@NotBlank String username, @NotBlank String password) {
}
