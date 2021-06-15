package com.roal.survey_engine.dto.response.mapping;

import com.roal.survey_engine.dto.response.ElementResponseDto;
import com.roal.survey_engine.dto.response.SurveyResponseDto;
import com.roal.survey_engine.entity.response.AbstractElementResponse;
import com.roal.survey_engine.entity.response.SurveyResponse;
import com.roal.survey_engine.entity.survey.Campaign;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public final class ResponseDtoMapper {

    public SurveyResponse mapSurveyResponseDtoToSurveyResponse(Campaign campaign, SurveyResponseDto surveyResponseDto) {

        var surveyResponse = new SurveyResponse();
        surveyResponse.setCampaign(campaign);
        var survey = campaign.getSurvey();
        List<AbstractElementResponse> elementResponseList = new ArrayList<>();

        for (ElementResponseDto elementResponseDto : surveyResponseDto.getElementResponseDtos()) {
            ResponseDtoMappingStrategy strategy = ResponseDtoMappingStrategyFactory.create(elementResponseDto);
            AbstractElementResponse elementResponse = strategy.map(survey, elementResponseDto);
            elementResponseList.add(elementResponse);
        }
        surveyResponse.setElementResponses(elementResponseList);
        return surveyResponse;

    }
}
