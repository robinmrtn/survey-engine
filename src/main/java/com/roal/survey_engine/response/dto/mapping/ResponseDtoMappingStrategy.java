package com.roal.survey_engine.response.dto.mapping;

import com.roal.survey_engine.response.dto.ElementResponseDto;
import com.roal.survey_engine.response.entity.AbstractElementResponse;
import com.roal.survey_engine.response.exception.InvalidDataFormatException;
import com.roal.survey_engine.survey.entity.Survey;
import com.roal.survey_engine.survey.entity.question.AbstractSurveyElement;

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

