package com.roal.survey_engine.domain.survey.dto.survey;

import com.roal.survey_engine.domain.survey.dto.survey.element.AbstractElementDto;

import java.util.List;

public record SurveyPageDto(int position, List<AbstractElementDto> surveyPageElements) {
}
