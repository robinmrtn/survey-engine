package com.roal.jsurvey.dto;

import com.roal.jsurvey.entity.questions.ClosedQuestionAnswer;
import com.roal.jsurvey.entity.questions.OpenQuestion;
import com.roal.jsurvey.entity.responses.AbstractElementResponse;
import com.roal.jsurvey.entity.responses.ClosedQuestionResponse;
import com.roal.jsurvey.entity.responses.OpenQuestionResponse;
import com.roal.jsurvey.entity.responses.SurveyResponse;
import com.roal.jsurvey.entity.survey.Survey;
import com.roal.jsurvey.entity.survey.SurveyPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.roal.jsurvey.entity.questions.ClosedQuestion.ClosedQuestionBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ResponseDtoMapperTest {

    ResponseDtoMapper responseDtoMapper = new ResponseDtoMapper();

    @Test
    void testDtoToEntityMapping() {
        // Arrange
        Survey survey = getTestSurvey();
        SurveyResponseDto surveyResponseDto = getSurveyResponseDto();
        // Act
        SurveyResponse surveyResponse = responseDtoMapper.mapSurveyResponseDtoToSurveyResponse(survey, surveyResponseDto);

        List<AbstractElementResponse> elementResponses = surveyResponse.getElementResponses();

        Optional<AbstractElementResponse> openQuestionResponse = elementResponses
                .stream()
                .filter(OpenQuestionResponse.class::isInstance)
                .filter(e -> ((OpenQuestionResponse) e).getOpenQuestion().getId() == 9)
                .findFirst();

        Optional<AbstractElementResponse> closedQuestionResponse = elementResponses.stream()
                .filter(ClosedQuestionResponse.class::isInstance)
                .filter(e -> ((ClosedQuestionResponse) e).getClosedQuestion().getId() == 10)
                .findFirst();
        // Assert
        assertTrue(openQuestionResponse.isPresent());
        assertTrue(openQuestionResponse.get() instanceof OpenQuestionResponse);
        assertEquals("This is an answer", ((OpenQuestionResponse) openQuestionResponse.get()).getValue());

        assertTrue(closedQuestionResponse.isPresent());
        assertTrue(closedQuestionResponse.get() instanceof ClosedQuestionResponse);
        assertTrue(((ClosedQuestionResponse) closedQuestionResponse.get()).getAnswers()
                .stream().allMatch(e -> e.getId() == 22L));
        assertEquals(1, ((ClosedQuestionResponse) closedQuestionResponse.get()).getAnswers().size());
    }

    private SurveyResponseDto getSurveyResponseDto() {
        var surveyResponseDto = new SurveyResponseDto();
        List<ElementResponseDto> elementResponseDtos = new ArrayList<>();
        elementResponseDtos.add(new OpenQuestionResponseDto(9, "This is an answer"));
        elementResponseDtos.add(new ClosedQuestionResponseDto(10, Set.of(22L)));
        surveyResponseDto.setElementResponseDtos(elementResponseDtos);
        return surveyResponseDto;
    }

    private Survey getTestSurvey() {

        var openQuestion = new OpenQuestion(9, "This is an open question?")
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

        return new Survey("This is a Survey")
                .addSurveyPage(firstSurveyPage);
    }
}
