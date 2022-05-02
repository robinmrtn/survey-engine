package com.roal.survey_engine.domain.survey.dto.survey;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public record CreateSurveyDto(@NotBlank String title, String description, String workspaceId,
                              @NotEmpty List<SurveyPageDto> surveyPages) {
}
