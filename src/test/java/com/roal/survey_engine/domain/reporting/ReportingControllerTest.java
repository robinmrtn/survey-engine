package com.roal.survey_engine.domain.reporting;

import com.roal.survey_engine.domain.response.entity.OpenNumericQuestionResponse;
import com.roal.survey_engine.domain.response.entity.SurveyResponse;
import com.roal.survey_engine.domain.response.repository.ResponseRepository;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.SurveyPage;
import com.roal.survey_engine.domain.survey.entity.question.AbstractSurveyElement;
import com.roal.survey_engine.domain.survey.entity.question.OpenNumericQuestion;
import com.roal.survey_engine.domain.survey.repository.CampaignRepository;
import com.roal.survey_engine.domain.survey.repository.SurveyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user")
public class ReportingControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    ResponseRepository responseRepository;

    @Test
    void getNumericElementReport() throws Exception {
        var survey = new Survey()
            .addSurveyPage(new SurveyPage()
                .addSurveyElement(new OpenNumericQuestion("question")));
        var campaign = new Campaign().setSurvey(survey);
        Survey savedSurvey = surveyRepository.saveAndFlush(survey);
        Campaign savedCampaign = campaignRepository.saveAndFlush(campaign);

        AbstractSurveyElement element = savedSurvey.getSurveyPages().get(0).getSurveyPageElements().get(0);

        var surveyResponse = new SurveyResponse()
            .setSurvey(survey)
            .addElement(new OpenNumericQuestionResponse()
                .setOpenNumericQuestion((OpenNumericQuestion) element).setAnswer(1.0));

        var surveyResponse2 = new SurveyResponse()
            .setSurvey(survey)
            .setCampaign(campaign)
            .addElement(new OpenNumericQuestionResponse()
                .setOpenNumericQuestion((OpenNumericQuestion) element).setAnswer(2.0));
        responseRepository.saveAllAndFlush(List.of(surveyResponse, surveyResponse2));

        MockHttpServletResponse response = mvc.perform(get("/api/campaigns/" + savedCampaign.getId() + "/" + element.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

        response.getContentAsString();
    }
}
