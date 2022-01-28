package com.roal.survey_engine.integration;

import com.roal.survey_engine.domain.response.repository.ResponseRepository;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.SurveyPage;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestion;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestionAnswer;
import com.roal.survey_engine.domain.survey.entity.question.OpenNumericQuestion;
import com.roal.survey_engine.domain.survey.entity.question.OpenTextQuestion;
import com.roal.survey_engine.domain.survey.repository.CampaignRepository;
import com.roal.survey_engine.domain.survey.repository.SurveyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SurveyResponseOutboundIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @BeforeEach
    public void setup() {
        var survey = createSurvey();
        Campaign campaign = createCampaign(survey);
    }

    @Test
    void getResponses() {
//        restTemplate.getForEntity("/api/responses/campaigns/")
    }

    private Campaign createCampaign(Survey survey) {
        var campaign = new Campaign()
                .setSurvey(survey);

        return campaignRepository.save(campaign);
    }

    private Survey createSurvey() {

        var openQuestion = new OpenTextQuestion("This is an open question?")
                .setPosition(1);

        var closedQuestion = new ClosedQuestion("This is an closed question")
                .addAnswer(new ClosedQuestionAnswer("first answer"))
                .addAnswer(new ClosedQuestionAnswer("second answer"));

        var openNumericQuestion = new OpenNumericQuestion("THis is a numeric question");

        var surveyPage = new SurveyPage()
                .addSurveyElement(openQuestion)
                .addSurveyElement(closedQuestion)
                .addSurveyElement(openNumericQuestion);

        var survey = new Survey()
                .setDescription("This is a Survey")
                .addSurveyPage(surveyPage);

        return surveyRepository.save(survey);
    }

}
