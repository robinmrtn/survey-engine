package com.roal.survey_engine.dto.response.mapping;

import com.roal.survey_engine.dto.response.ElementResponseDto;
import com.roal.survey_engine.dto.response.OpenQuestionResponseDto;
import com.roal.survey_engine.entity.question.AbstractSurveyElement;
import com.roal.survey_engine.entity.question.OpenTextQuestion;
import com.roal.survey_engine.entity.response.OpenTextQuestionResponse;
import com.roal.survey_engine.entity.survey.Survey;

class OpenTextQuestionResponseDtoMappingStrategy implements ResponseDtoMappingStrategy {

    @Override
    public OpenTextQuestionResponse map(Survey survey, ElementResponseDto dto) {
        return getOpenQuestionResponse(survey, (OpenQuestionResponseDto) dto);
    }

    private OpenTextQuestionResponse getOpenQuestionResponse(Survey survey, OpenQuestionResponseDto elementResponseDto) {

        var elementResponse = new OpenTextQuestionResponse();
        AbstractSurveyElement surveyElement =
                findSurveyElementById(survey, elementResponseDto.getId());

        elementResponse.setAnswer(elementResponseDto.getValue());
        elementResponse.setOpenQuestion((OpenTextQuestion) surveyElement);

        return elementResponse;
    }


}
