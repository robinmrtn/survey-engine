package com.roal.survey_engine.domain.survey.dto.campaign;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record CampaignDto(String id, LocalDateTime from, LocalDateTime to, @NotBlank String title, boolean active,
                          boolean hidden, String surveyId) {
}
