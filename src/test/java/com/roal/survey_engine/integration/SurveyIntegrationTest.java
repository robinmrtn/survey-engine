package com.roal.survey_engine.integration;

import com.roal.survey_engine.entity.question.ClosedQuestion;
import com.roal.survey_engine.entity.question.ClosedQuestionAnswer;
import com.roal.survey_engine.entity.question.OpenTextQuestion;
import com.roal.survey_engine.entity.survey.Survey;
import com.roal.survey_engine.entity.survey.SurveyPage;
import com.roal.survey_engine.repository.SurveyRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SurveyIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    SurveyRepository surveyRepository;

    private static Survey survey;

    @BeforeAll
    public static void setup() {
        var openQuestion = new OpenTextQuestion("This is an open question?");
        var closedQuestion = new ClosedQuestion("This is a closed question?")
                .addAnswer(new ClosedQuestionAnswer("first answer"))
                .addAnswer(new ClosedQuestionAnswer("second answer"));

        survey = new Survey("This is a Survey")
                .addSurveyPage(new SurveyPage().addSurveyElement(openQuestion))
                .addSurveyPage(new SurveyPage().addSurveyElement(closedQuestion));
    }

    @Test
    @DisplayName("should return 201 when new Survey is submitted")
    void postNewSurvey() {

        ResponseEntity<Survey> responseEntity = restTemplate
                .postForEntity("/surveys/", survey, Survey.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

    }


}
