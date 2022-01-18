package com.roal.survey_engine.domain.survey.dto;

import java.util.List;

public record SurveyDto(long id, String title, String description, List<SurveyPageDto> surveyPages) {
}
