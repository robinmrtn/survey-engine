package com.roal.survey_engine.domain.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

public record UserRegistrationDto(@Size(min = 3, max = 32) String username,
                                  @Size(min = 8) String password,
                                  @Size(min = 8) String passwordRepeated,
                                  @Email String email,
                                  @NotEmpty Set<String> roles) {
}
