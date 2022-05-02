package com.roal.survey_engine.domain.response.dto;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public record CreateSurveyResponseDto(@NotEmpty List<ElementResponseDto> elementResponseDtos) {
}
