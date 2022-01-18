package com.roal.survey_engine.domain.survey.dto;

import com.roal.survey_engine.domain.survey.dto.element.AbstractElementDto;

import java.util.List;

public record SurveyPageDto(int position, List<AbstractElementDto> surveyPageElements) {
}
