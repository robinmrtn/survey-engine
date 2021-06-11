package com.roal.jsurvey.integration;

import com.roal.jsurvey.dto.ElementResponseDto;
import com.roal.jsurvey.dto.OpenQuestionResponseDto;
import com.roal.jsurvey.dto.SurveyResponseDto;
import com.roal.jsurvey.entity.questions.AbstractSurveyElement;
import com.roal.jsurvey.entity.questions.OpenQuestion;
import com.roal.jsurvey.entity.survey.Survey;
import com.roal.jsurvey.entity.survey.SurveyPage;
import com.roal.jsurvey.repository.SurveyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SurveyResponseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SurveyRepository surveyRepository;

    @Test
    void testPostResponseDto_Success() {

        Survey survey = createSurvey();
        long surveyId = survey.getId();
        long openQuestionId = getSurveyElementId(survey, OpenQuestion.class, 0);

        ResponseEntity<SurveyResponseDto> responseEntity =
                restTemplate.postForEntity("/responses/surveys/" + surveyId,
                        createSurveyResponseDto(openQuestionId), SurveyResponseDto.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testPostResponseDto_WrongFormat() {

        Survey survey = createSurvey();
        long surveyId = survey.getId();
        long openQuestionId = getSurveyElementId(survey, OpenQuestion.class, 0) + 1;

        ResponseEntity<SurveyResponseDto> responseEntity =
                restTemplate.postForEntity("/responses/surveys/" + surveyId,
                        createSurveyResponseDto(openQuestionId), SurveyResponseDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    private SurveyResponseDto createSurveyResponseDto(long openQuestionId) {

        var surveyResponseDto = new SurveyResponseDto();
        List<ElementResponseDto> elementDtoList = new ArrayList<>();
        elementDtoList.add(new OpenQuestionResponseDto(openQuestionId, "This is an answer to an open question."));
        surveyResponseDto.setElementResponseDtos(elementDtoList);

        return surveyResponseDto;
    }

    private Survey createSurvey() {

        var openQuestion = new OpenQuestion("This is an open question?")
                .setPosition(1);

        var surveyPage = new SurveyPage()
                .addSurveyElement(openQuestion);

        var survey = new Survey()
                .setDescription("This is a Survey")
                .addSurveyPage(surveyPage);
        return surveyRepository.save(survey);
    }

    private long getSurveyElementId(Survey survey, Class clazz, long skip) {
        return survey.getSurveyPages().stream()
                .flatMap(e -> e.getSurveyPageElements().stream())
                .filter(clazz::isInstance)
                .skip(skip)
                .mapToLong(AbstractSurveyElement::getId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(clazz.getName() + " was not found in " +
                        "Survey Elements."));
    }

}
