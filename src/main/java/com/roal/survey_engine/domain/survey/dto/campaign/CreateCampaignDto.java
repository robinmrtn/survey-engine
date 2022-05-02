package com.roal.survey_engine.domain.survey.dto.campaign;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record CreateCampaignDto(LocalDateTime from, LocalDateTime to, @NotBlank String title, boolean active,
                                boolean hidden, long surveyId) {
}
