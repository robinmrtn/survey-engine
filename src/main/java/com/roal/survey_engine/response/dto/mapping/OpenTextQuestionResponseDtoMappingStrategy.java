package com.roal.survey_engine.response.dto.mapping;

import com.roal.survey_engine.response.dto.ElementResponseDto;
import com.roal.survey_engine.response.dto.OpenQuestionResponseDto;
import com.roal.survey_engine.response.entity.OpenTextQuestionResponse;
import com.roal.survey_engine.survey.entity.Survey;
import com.roal.survey_engine.survey.entity.question.AbstractSurveyElement;
import com.roal.survey_engine.survey.entity.question.OpenTextQuestion;

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
