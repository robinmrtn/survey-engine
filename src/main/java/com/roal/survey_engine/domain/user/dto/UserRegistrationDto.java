package com.roal.survey_engine.domain.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

public record UserRegistrationDto(@NotBlank String username, @NotBlank String password, @NotEmpty Set<String> roles) {
}
