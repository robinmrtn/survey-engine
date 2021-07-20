package com.roal.survey_engine.integration;

//import com.roal.survey_engine.dto.survey.SurveyDto;
import com.roal.survey_engine.entity.question.OpenTextQuestion;
import com.roal.survey_engine.entity.survey.Survey;
import com.roal.survey_engine.entity.survey.SurveyPage;
import com.roal.survey_engine.repository.SurveyRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    SurveyRepository surveyRepository;

    @Disabled
    @Test
    @DisplayName("should return 201 when new Survey is submitted")
    void postNewSurvey() {

        ResponseEntity<Survey> responseEntity = restTemplate
                .postForEntity("api/surveys/", createSurvey(), Survey.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

    }

    private Survey createSurvey() {

        var openQuestion = new OpenTextQuestion("This is an open question?")
                .setPosition(1);

        var surveyPage = new SurveyPage()
                .addSurveyElement(openQuestion);

        var survey = new Survey()
                .setDescription("This is a Survey")
                .addSurveyPage(surveyPage);

        return surveyRepository.save(survey);
    }

}
