package com.roal.survey_engine.integration;

//import com.roal.survey_engine.dto.survey.SurveyDto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roal.survey_engine.domain.survey.dto.SurveyDto;
import com.roal.survey_engine.domain.survey.dto.SurveyDtoMapper;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.SurveyPage;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestion;
import com.roal.survey_engine.domain.survey.entity.question.OpenTextQuestion;
import com.roal.survey_engine.domain.survey.repository.CampaignRepository;
import com.roal.survey_engine.domain.survey.repository.SurveyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

    @Autowired
    SurveyDtoMapper mapper;

    @Autowired
    ObjectMapper objectMapper;

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

    @Test
    @DisplayName("should return 201 when new Survey is submitted")
    void postNewSurvey() throws Exception {

        SurveyDto surveyDto = mapper.entityToDto(createSurvey());
        String json = objectMapper.writeValueAsString(surveyDto);
        MvcResult mvcResult = mvc.perform(post("/api/surveys/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());

    }

    private Survey createSurvey() {

        return new Survey("This is a survey")
                .addSurveyPage(new SurveyPage()
                        .addSurveyElement(new ClosedQuestion("This is a closed question")));
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
