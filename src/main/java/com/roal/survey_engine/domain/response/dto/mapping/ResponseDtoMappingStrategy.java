package com.roal.survey_engine.domain.response.dto.mapping;

import com.roal.survey_engine.domain.response.dto.ElementResponseDto;
import com.roal.survey_engine.domain.response.entity.AbstractElementResponse;
import com.roal.survey_engine.domain.response.exception.InvalidDataFormatException;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.question.AbstractSurveyElement;

public interface ResponseDtoMappingStrategy {
    AbstractElementResponse map(Survey survey, ElementResponseDto dto);

    default AbstractSurveyElement findSurveyElementById(Survey survey, long id) {
        return survey.getSurveyPages()
                .stream()
                .flatMap(p -> p.getSurveyPageElements().stream())
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(InvalidDataFormatException::new);
    }
}

