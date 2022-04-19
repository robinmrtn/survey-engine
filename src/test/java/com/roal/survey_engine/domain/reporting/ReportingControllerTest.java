package com.roal.survey_engine.domain.reporting;

import com.roal.survey_engine.domain.response.entity.OpenNumericQuestionResponse;
import com.roal.survey_engine.domain.response.entity.SurveyResponse;
import com.roal.survey_engine.domain.response.repository.ResponseRepository;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.DateRange;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.SurveyPage;
import com.roal.survey_engine.domain.survey.entity.question.AbstractSurveyElement;
import com.roal.survey_engine.domain.survey.entity.question.OpenNumericQuestion;
import com.roal.survey_engine.domain.survey.repository.CampaignRepository;
import com.roal.survey_engine.domain.survey.repository.SurveyRepository;
import org.hashids.Hashids;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    @Qualifier("campaignHashids")
    Hashids hashids;

    @Test
    void getNumericElementReport() throws Exception {
        var survey = new Survey()
            .addSurveyPage(new SurveyPage()
                .addSurveyElement(new OpenNumericQuestion("question")));
        var campaign = new Campaign().setSurvey(survey)
            .setDateRange(new DateRange(LocalDateTime.now(), LocalDateTime.MAX));
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

        String campaignHashid = hashids.encode(savedCampaign.getId());

        mvc.perform(get("/api/campaigns/" + campaignHashid + "/report")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json("[{\"elementId\":3,\"count\":2,\"avg\":1.5,\"min\":1.0,\"max\":2.0,\"median\":1.5,\"percentile25\":0.0,\"percentile75\":0.0,\"sd\":0.7071067811865476}]"));
    }
}
