package com.roal.survey_engine.domain.survey.dto.workspace;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

public record WorkspaceDto(String id, @NotBlank String title, Collection<String> userIds,
                           Collection<String> surveyIds) {
}
