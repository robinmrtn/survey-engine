package com.roal.survey_engine.integration;

import com.roal.survey_engine.dto.survey.SurveyDto;
import com.roal.survey_engine.repository.SurveyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SurveyIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    SurveyRepository surveyRepository;

    @Test
    @DisplayName("should return 201 when new Survey is submitted")
    void postNewSurvey() {

        ResponseEntity<SurveyDto> responseEntity = restTemplate
                .postForEntity("/survey/", createTestDto(), SurveyDto.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

    }

    SurveyDto createTestDto() {
        return new SurveyDto();
    }
}
