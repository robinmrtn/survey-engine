package com.roal.survey_engine.response.dto.mapping;

import com.roal.survey_engine.response.dto.ClosedQuestionResponseDto;
import com.roal.survey_engine.response.dto.ElementResponseDto;
import com.roal.survey_engine.response.entity.ClosedQuestionResponse;
import com.roal.survey_engine.survey.entity.Survey;
import com.roal.survey_engine.survey.entity.question.AbstractSurveyElement;
import com.roal.survey_engine.survey.entity.question.ClosedQuestion;
import com.roal.survey_engine.survey.entity.question.ClosedQuestionAnswer;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClosedQuestionResponseDtoMappingStrategy implements ResponseDtoMappingStrategy {
    @Override
    public ClosedQuestionResponse map(Survey survey, ElementResponseDto dto) {
        return getClosedQuestionResponse(survey, (ClosedQuestionResponseDto) dto);
    }

    private ClosedQuestionResponse getClosedQuestionResponse(Survey survey,
                                                             ClosedQuestionResponseDto elementResponseDto) {

        var elementResponse = new ClosedQuestionResponse();
        AbstractSurveyElement surveyElement =
                findSurveyElementById(survey, elementResponseDto.getId());

        List<ClosedQuestionAnswer> closedQuestionAnswers =
                getClosedQuestionAnswers(survey, elementResponseDto);

        elementResponse.setAnswers(closedQuestionAnswers);
        elementResponse.setClosedQuestion((ClosedQuestion) surveyElement);

        return elementResponse;
    }

    private List<ClosedQuestionAnswer> getClosedQuestionAnswers(Survey survey,
                                                                ClosedQuestionResponseDto elementResponseDto) {
        Set<Long> answerIds = elementResponseDto.getAnswers();

        return survey.getSurveyPages()
                .stream()
                .flatMap(surveyPage -> surveyPage.getSurveyPageElements().stream())
                .filter(ClosedQuestion.class::isInstance)
                .filter(e -> e.getId() == elementResponseDto.getId())
                .flatMap(e -> ((ClosedQuestion) e).getAnswers().stream())
                .filter(closedQuestionAnswer -> answerIds.contains(closedQuestionAnswer.getId()))
                .collect(Collectors.toList());
    }


}
