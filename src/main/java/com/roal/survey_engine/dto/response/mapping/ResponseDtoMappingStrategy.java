package com.roal.survey_engine.dto.response.mapping;

import com.roal.survey_engine.dto.response.ElementResponseDto;
import com.roal.survey_engine.entity.question.AbstractSurveyElement;
import com.roal.survey_engine.entity.response.AbstractElementResponse;
import com.roal.survey_engine.entity.survey.Survey;
import com.roal.survey_engine.exception.InvalidDataFormatException;

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

