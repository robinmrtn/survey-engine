package com.roal.survey_engine.domain.response;

import com.roal.survey_engine.domain.response.dto.ClosedQuestionResponseDto;
import com.roal.survey_engine.domain.response.dto.CreateSurveyResponseDto;
import com.roal.survey_engine.domain.response.dto.ElementResponseDto;
import com.roal.survey_engine.domain.response.dto.OpenQuestionResponseDto;
import com.roal.survey_engine.domain.response.dto.mapping.ResponseDtoMapper;
import com.roal.survey_engine.domain.response.entity.AbstractElementResponse;
import com.roal.survey_engine.domain.response.entity.ClosedQuestionResponse;
import com.roal.survey_engine.domain.response.entity.OpenTextQuestionResponse;
import com.roal.survey_engine.domain.response.entity.SurveyResponse;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.SurveyPage;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestionAnswer;
import com.roal.survey_engine.domain.survey.entity.question.OpenTextQuestion;
import org.hashids.Hashids;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.roal.survey_engine.domain.survey.entity.question.ClosedQuestion.ClosedQuestionBuilder;
import static org.junit.jupiter.api.Assertions.*;

class ResponseDtoMapperTest {

    ResponseDtoMapper responseDtoMapper = new ResponseDtoMapper(new Hashids());

    @Test
    void testDtoToEntityMapping() {

        // Arrange
        Campaign campaign = getTestCampaign();
        CreateSurveyResponseDto surveyResponseDto = getSurveyResponseDto();

        // Act
        SurveyResponse surveyResponse = responseDtoMapper.dtoToEntity(campaign, surveyResponseDto);
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

    private CreateSurveyResponseDto getSurveyResponseDto() {
        List<ElementResponseDto> elementResponseDtos = new ArrayList<>();
        elementResponseDtos.add(new OpenQuestionResponseDto(9, "This is an answer"));
        elementResponseDtos.add(new ClosedQuestionResponseDto(10, Set.of(22L)));
        return new CreateSurveyResponseDto(elementResponseDtos);
    }

    private Campaign getTestCampaign() {

        var openQuestion = new OpenTextQuestion(9, "This is an open question?")
                .setPosition(1);

        var closedQuestion = new ClosedQuestionBuilder()
                .setQuestion("This is a closed question?")
                .setId(10)
                .withAnswers()
                .addAnswer(new ClosedQuestionAnswer(21L, "First answer"))
                .addAnswer(new ClosedQuestionAnswer(22L, "Second answer"))
                .addAnswer(new ClosedQuestionAnswer(23L, "Third answer"))
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
