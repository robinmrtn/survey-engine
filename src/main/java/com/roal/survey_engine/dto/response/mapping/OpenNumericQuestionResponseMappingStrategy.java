package com.roal.survey_engine.dto.response.mapping;

import com.roal.survey_engine.dto.response.ElementResponseDto;
import com.roal.survey_engine.dto.response.OpenNumericQuestionResponseDto;
import com.roal.survey_engine.entity.question.AbstractSurveyElement;
import com.roal.survey_engine.entity.question.OpenNumericQuestion;
import com.roal.survey_engine.entity.response.OpenNumericQuestionResponse;
import com.roal.survey_engine.entity.survey.Survey;

public class OpenNumericQuestionResponseMappingStrategy implements ResponseDtoMappingStrategy {

    private OpenNumericQuestionResponse getOpenNumericQuestionResponse(Survey survey, OpenNumericQuestionResponseDto dto) {
        var elementResponse = new OpenNumericQuestionResponse();
        AbstractSurveyElement surveyElement =
                findSurveyElementById(survey, dto.getId());

        elementResponse.setAnswer(dto.getValue());
        elementResponse.setOpenNumericQuestion((OpenNumericQuestion) surveyElement);

        return elementResponse;
    }

    @Override
    public OpenNumericQuestionResponse map(Survey survey, ElementResponseDto dto) {
        return getOpenNumericQuestionResponse(survey, (OpenNumericQuestionResponseDto) dto);
    }
}
