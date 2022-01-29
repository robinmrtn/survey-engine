package com.roal.survey_engine.domain.survey.dto.campaign;

import java.time.LocalDateTime;

public record CampaignDto(long id, LocalDateTime from, LocalDateTime to, String title, boolean active,
                          boolean hidden, long surveyId) {
}
