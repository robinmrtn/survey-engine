package com.roal.survey_engine.repository;

import com.roal.survey_engine.response.entity.ClosedQuestionResponse;
import com.roal.survey_engine.response.entity.OpenTextQuestionResponse;
import com.roal.survey_engine.response.entity.SurveyResponse;
import com.roal.survey_engine.response.repository.ResponseRepository;
import com.roal.survey_engine.survey.entity.Campaign;
import com.roal.survey_engine.survey.entity.Survey;
import com.roal.survey_engine.survey.entity.SurveyPage;
import com.roal.survey_engine.survey.entity.question.ClosedQuestion;
import com.roal.survey_engine.survey.entity.question.ClosedQuestionAnswer;
import com.roal.survey_engine.survey.entity.question.OpenTextQuestion;
import com.roal.survey_engine.survey.repository.CampaignRepository;
import com.roal.survey_engine.survey.repository.SurveyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SurveyResponseJpaRepositoryTest {

    @Autowired
    ResponseRepository responseRepository;

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    TestEntityManager testEntityManager;


    @Test
    void testInsertResponse() {
        Survey survey = getSurvey();

        surveyRepository.save(survey);

        long surveyId = survey.getId();

        flushRepositories();

        Optional<Survey> receivedSurvey = surveyRepository.findById(surveyId);

        assertTrue(receivedSurvey.isPresent());

        var response = new SurveyResponse();
        response.setSurvey(receivedSurvey.get());
        var elementResponse1 = new OpenTextQuestionResponse();
        elementResponse1.setAnswer("This is an answer!");
        response.setElementResponses(List.of(elementResponse1));

        responseRepository.save(response);

        long responseId = response.getId();

        flushRepositories();

        Optional<SurveyResponse> byId = responseRepository.findById(responseId);
        assertAll(() -> assertTrue(byId.isPresent()),
                () -> assertEquals(response, byId.get()),
                () -> assertNotSame(response, byId.get()));
    }

    @Test
    public void testReceiveResponse() {

        var openQuestion = new OpenTextQuestion("This is an open question?");
        var closedQuestion = new ClosedQuestion("This is a closed question?")
                .addAnswer(new ClosedQuestionAnswer("first Answer"))
                .addAnswer(new ClosedQuestionAnswer("second Answer"));
        var firstSurveyPage = new SurveyPage()
                .addSurveyElement(openQuestion)
                .addSurveyElement(closedQuestion);

        var survey = new Survey("This is a Survey")
                .addSurveyPage(firstSurveyPage);

        var campaign = new Campaign().setSurvey(survey);

        surveyRepository.save(survey);
        campaignRepository.save(campaign);

        long id = campaign.getId();

       // flushRepositories();

        //campaign = campaignRepository.findById(id).get();

        var surveyResponse = new SurveyResponse().setCampaign(campaign)
                .addElement(new ClosedQuestionResponse().setClosedQuestion(closedQuestion)
                        .setAnswers(closedQuestion.getAnswers()))
                .addElement(new OpenTextQuestionResponse().setOpenQuestion(openQuestion).setAnswer("answer"));

        responseRepository.save(surveyResponse);

        long respId = surveyResponse.getId();

      //  flushRepositories();

        Optional<SurveyResponse> byId = responseRepository.findById(respId);

        assertTrue(byId.isPresent());

    }

    private Campaign getCampaign() {
        return new Campaign().setSurvey(getSurvey());
    }

    private Survey getSurvey() {

        var openQuestion = new OpenTextQuestion("This is an open question?");
        var closedQuestion = new ClosedQuestion("This is a closed question?")
                .addAnswer(new ClosedQuestionAnswer("first Answer").setId(44))
                .addAnswer(new ClosedQuestionAnswer("second Answer").setId(45));
        var firstSurveyPage = new SurveyPage()
                .addSurveyElement(openQuestion)
                .addSurveyElement(closedQuestion);

        return new Survey("This is a Survey")
                .addSurveyPage(firstSurveyPage);
    }

    private void flushRepositories() {
        testEntityManager.flush();
        testEntityManager.clear();
    }
}
