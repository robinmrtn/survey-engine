package com.roal.survey_engine.integration;

//import com.roal.survey_engine.dto.survey.SurveyDto;

import com.roal.survey_engine.survey.entity.Campaign;
import com.roal.survey_engine.survey.entity.Survey;
import com.roal.survey_engine.survey.entity.SurveyPage;
import com.roal.survey_engine.survey.entity.question.OpenTextQuestion;
import com.roal.survey_engine.survey.repository.CampaignRepository;
import com.roal.survey_engine.survey.repository.SurveyRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SurveyIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    MockMvc mvc;

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    CampaignRepository campaignRepository;

    @Test
    void getSurvey() throws Exception {

        Campaign campaign = createCampaign();
        MockHttpServletResponse response = mvc.perform(get("/api/surveys/" + campaign.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertAll(() -> assertEquals(HttpStatus.OK.value(), response.getStatus()));
    }

    @Test
    void getSurveys() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/api/surveys")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Disabled
    @Test
    @DisplayName("should return 201 when new Survey is submitted")
    void postNewSurvey() {

        ResponseEntity<Survey> responseEntity = restTemplate
                .postForEntity("/api/surveys/", createCampaign(), Survey.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

    }

    private Campaign createCampaign() {

        var openQuestion = new OpenTextQuestion("This is an open question?")
                .setPosition(1);

        var surveyPage = new SurveyPage()
                .addSurveyElement(openQuestion);

        var survey = new Survey()
                .setDescription("This is a Survey")
                .addSurveyPage(surveyPage);

        var campaign = new Campaign().setSurvey(survey);
        surveyRepository.saveAndFlush(survey);
        return campaignRepository.saveAndFlush(campaign);
    }

}
