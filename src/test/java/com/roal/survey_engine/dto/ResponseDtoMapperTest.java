package com.roal.survey_engine.dto;

import com.roal.survey_engine.dto.response.ClosedQuestionResponseDto;
import com.roal.survey_engine.dto.response.ElementResponseDto;
import com.roal.survey_engine.dto.response.OpenQuestionResponseDto;
import com.roal.survey_engine.dto.response.SurveyResponseDto;
import com.roal.survey_engine.dto.response.mapping.ResponseDtoMapper;
import com.roal.survey_engine.entity.question.ClosedQuestionAnswer;
import com.roal.survey_engine.entity.question.OpenTextQuestion;
import com.roal.survey_engine.entity.response.AbstractElementResponse;
import com.roal.survey_engine.entity.response.ClosedQuestionResponse;
import com.roal.survey_engine.entity.response.OpenTextQuestionResponse;
import com.roal.survey_engine.entity.response.SurveyResponse;
import com.roal.survey_engine.entity.survey.Campaign;
import com.roal.survey_engine.entity.survey.Survey;
import com.roal.survey_engine.entity.survey.SurveyPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.roal.survey_engine.entity.question.ClosedQuestion.ClosedQuestionBuilder;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ResponseDtoMapperTest {

    ResponseDtoMapper responseDtoMapper = new ResponseDtoMapper();

    @Test
    void testDtoToEntityMapping() {
        // Arrange
        Campaign campaign = getTestCampaign();
        SurveyResponseDto surveyResponseDto = getSurveyResponseDto();
        // Act
        SurveyResponse surveyResponse = responseDtoMapper.mapSurveyResponseDtoToSurveyResponse(campaign, surveyResponseDto);

        List<AbstractElementResponse> elementResponses = surveyResponse.getElementResponses();

        Optional<AbstractElementResponse> openQuestionResponse = elementResponses
                .stream()
                .filter(OpenTextQuestionResponse.class::isInstance)
                .filter(e -> ((OpenTextQuestionResponse) e).getOpenQuestion().getId() == 9)
                .findFirst();

        Optional<AbstractElementResponse> closedQuestionResponse = elementResponses.stream()
                .filter(ClosedQuestionResponse.class::isInstance)
                .filter(e -> ((ClosedQuestionResponse) e).getClosedQuestion().getId() == 10)
                .findFirst();
        // Assert

        assertAll(() -> assertTrue(openQuestionResponse.isPresent()),
                () -> assertTrue(openQuestionResponse.get() instanceof OpenTextQuestionResponse),
                () -> assertEquals("This is an answer",
                        ((OpenTextQuestionResponse) openQuestionResponse.get()).getAnswer()),
                () -> assertTrue(closedQuestionResponse.isPresent()),
                () -> assertTrue(closedQuestionResponse.get() instanceof ClosedQuestionResponse),
                () -> assertTrue(((ClosedQuestionResponse) closedQuestionResponse.get()).getAnswers()
                        .stream().allMatch(e -> e.getId() == 22L)),
                () -> assertEquals(1,
                        ((ClosedQuestionResponse) closedQuestionResponse.get()).getAnswers().size()));
    }

    private SurveyResponseDto getSurveyResponseDto() {
        var surveyResponseDto = new SurveyResponseDto();
        List<ElementResponseDto> elementResponseDtos = new ArrayList<>();
        elementResponseDtos.add(new OpenQuestionResponseDto(9, "This is an answer"));
        elementResponseDtos.add(new ClosedQuestionResponseDto(10, Set.of(22L)));
        surveyResponseDto.setElementResponseDtos(elementResponseDtos);
        return surveyResponseDto;
    }

    private Campaign getTestCampaign() {

        var openQuestion = new OpenTextQuestion(9, "This is an open question?")
                .setPosition(1);

        var closedQuestion = new ClosedQuestionBuilder()
                .setQuestion("This is a closed question?")
                .setId(10)
                .withAnswers()
                .addAnswer(new ClosedQuestionAnswer(21, "First answer"))
                .addAnswer(new ClosedQuestionAnswer(22, "Second answer"))
                .addAnswer(new ClosedQuestionAnswer(23, "Third answer"))
                .build()
                .build();

        var firstSurveyPage = new SurveyPage()
                .addSurveyElement(openQuestion)
                .addSurveyElement(closedQuestion);

        return new Campaign()
                .setSurvey(new Survey("This is a Survey")
                        .addSurveyPage(firstSurveyPage));

    }
}
