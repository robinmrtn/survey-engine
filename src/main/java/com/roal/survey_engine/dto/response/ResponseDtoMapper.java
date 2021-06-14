package com.roal.survey_engine.dto.response;

import com.roal.survey_engine.entity.question.AbstractSurveyElement;
import com.roal.survey_engine.entity.question.ClosedQuestion;
import com.roal.survey_engine.entity.question.ClosedQuestionAnswer;
import com.roal.survey_engine.entity.question.OpenTextQuestion;
import com.roal.survey_engine.entity.response.AbstractElementResponse;
import com.roal.survey_engine.entity.response.ClosedQuestionResponse;
import com.roal.survey_engine.entity.response.OpenTextQuestionResponse;
import com.roal.survey_engine.entity.response.SurveyResponse;
import com.roal.survey_engine.entity.survey.Campaign;
import com.roal.survey_engine.entity.survey.Survey;
import com.roal.survey_engine.exception.InvalidDataFormatException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public final class ResponseDtoMapper {

    public SurveyResponse mapSurveyResponseDtoToSurveyResponse(Campaign campaign, SurveyResponseDto surveyResponseDto) {

        var surveyResponse = new SurveyResponse();
        surveyResponse.setCampaign(campaign);
        var survey = campaign.getSurvey();
        List<AbstractElementResponse> elementResponseList = new ArrayList<>();

        for (ElementResponseDto elementResponseDto : surveyResponseDto.getElementResponseDtos()) {

            if (elementResponseDto instanceof OpenQuestionResponseDto) {
                OpenTextQuestionResponse openQuestionResponse =
                        getOpenQuestionResponse(survey, (OpenQuestionResponseDto) elementResponseDto);
                elementResponseList.add(openQuestionResponse);
            } else if (elementResponseDto instanceof ClosedQuestionResponseDto) {
                ClosedQuestionResponse closedQuestionResponse =
                        getClosedQuestionResponse(survey, (ClosedQuestionResponseDto) elementResponseDto);
                elementResponseList.add(closedQuestionResponse);
            }
        }
        surveyResponse.setElementResponses(elementResponseList);

        return surveyResponse;
    }

    private ClosedQuestionResponse getClosedQuestionResponse(Survey survey,
                                                             ClosedQuestionResponseDto elementResponseDto) {

        var elementResponse = new ClosedQuestionResponse();
        AbstractSurveyElement surveyElement =
                findSurveyElementById(survey, elementResponseDto.getElementId());

        List<ClosedQuestionAnswer> closedQuestionAnswers =
                getClosedQuestionAnswers(survey, elementResponseDto);

        elementResponse.setAnswers(closedQuestionAnswers);
        elementResponse.setClosedQuestion((ClosedQuestion) surveyElement);

        return elementResponse;
    }

    private OpenTextQuestionResponse getOpenQuestionResponse(Survey survey, OpenQuestionResponseDto elementResponseDto) {

        var elementResponse = new OpenTextQuestionResponse();
        AbstractSurveyElement surveyElement =
                findSurveyElementById(survey, elementResponseDto.getElementId());

        elementResponse.setAnswer(elementResponseDto.getValue());
        elementResponse.setOpenQuestion((OpenTextQuestion) surveyElement);

        return elementResponse;
    }

    private List<ClosedQuestionAnswer> getClosedQuestionAnswers(Survey survey,
                                                                ClosedQuestionResponseDto elementResponseDto) {

        Set<Long> answerIds = elementResponseDto.getAnswers();

        return survey.getSurveyPages()
                .stream()
                .flatMap(surveyPage -> surveyPage.getSurveyPageElements().stream())
                .filter(ClosedQuestion.class::isInstance)
                .filter(e -> e.getId() == elementResponseDto.getElementId())
                .flatMap(e -> ((ClosedQuestion) e).getAnswers().stream())
                .filter(closedQuestionAnswer -> answerIds.contains(closedQuestionAnswer.getId()))
                .collect(Collectors.toList());
    }

    private AbstractSurveyElement findSurveyElementById(Survey survey, long id) {
        return survey.getSurveyPages()
                .stream()
                .flatMap(p -> p.getSurveyPageElements().stream())
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(InvalidDataFormatException::new);
    }

}
