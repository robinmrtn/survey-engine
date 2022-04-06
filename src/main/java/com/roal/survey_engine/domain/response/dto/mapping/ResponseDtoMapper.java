package com.roal.survey_engine.domain.response.dto.mapping;

import com.roal.survey_engine.domain.response.dto.*;
import com.roal.survey_engine.domain.response.entity.*;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestionAnswer;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public final class ResponseDtoMapper {

    private final Hashids responseHashids;

    public ResponseDtoMapper(@Qualifier("responseHashids") Hashids responseHashids) {
        this.responseHashids = responseHashids;
    }

    public SurveyResponse dtoToEntity(Campaign campaign, SurveyResponseDto surveyResponseDto) {

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
        surveyResponse.setSurvey(survey);
        return surveyResponse;

    }

    public SurveyResponseDto entityToDto(SurveyResponse surveyResponse) {

        List<ElementResponseDto> elementResponseDtos = surveyResponse.getElementResponses()
                .stream()
                .map(this::mapElementResponseToElementResponseDto)
                .collect(Collectors.toList());

        var surveyResponseDto = new SurveyResponseDto();
        surveyResponseDto.setId(responseHashids.encode(surveyResponse.getId()));
        surveyResponseDto.setElementResponseDtos(elementResponseDtos);

        return surveyResponseDto;

    }

    private ElementResponseDto mapElementResponseToElementResponseDto(AbstractElementResponse elementResponse) {

        if (elementResponse instanceof OpenNumericQuestionResponse) {
            return new OpenNumericQuestionResponseDto()
                    .setValue(((OpenNumericQuestionResponse) elementResponse).getAnswer())
                    .setElementId(((OpenNumericQuestionResponse) elementResponse).getOpenNumericQuestion().getId());
        } else if (elementResponse instanceof OpenTextQuestionResponse) {
            return new OpenQuestionResponseDto()
                    .setValue(((OpenTextQuestionResponse) elementResponse).getAnswer())
                    .setElementId(((OpenTextQuestionResponse) elementResponse).getOpenQuestion().getId());
        } else if (elementResponse instanceof ClosedQuestionResponse) {
            Set<Long> answerIds = ((ClosedQuestionResponse) elementResponse).getAnswers()
                    .stream()
                    .map(ClosedQuestionAnswer::getId)
                    .collect(Collectors.toSet());
            return new ClosedQuestionResponseDto()
                    .setAnswers(answerIds)
                    .setElementId(((ClosedQuestionResponse) elementResponse).getClosedQuestion().getId());
        } else {
            throw new RuntimeException();
        }
    }
}

