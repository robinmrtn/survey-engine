package com.roal.survey_engine.domain.response;

import com.roal.survey_engine.domain.response.entity.ClosedQuestionResponse;
import com.roal.survey_engine.domain.response.entity.OpenTextQuestionResponse;
import com.roal.survey_engine.domain.response.entity.SurveyResponse;
import com.roal.survey_engine.domain.response.repository.ResponseRepository;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.SurveyPage;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestion;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestionAnswer;
import com.roal.survey_engine.domain.survey.entity.question.OpenTextQuestion;
import com.roal.survey_engine.domain.survey.repository.CampaignRepository;
import com.roal.survey_engine.domain.survey.repository.SurveyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        response.setElementResponses(Set.of(elementResponse1));

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

        flushRepositories();

        //campaign = campaignRepository.findById(id).get();

        var surveyResponse = new SurveyResponse().setCampaign(campaign)
                .addElement(new ClosedQuestionResponse().setClosedQuestion(closedQuestion)
                        .setAnswers(closedQuestion.getAnswers()))
                .addElement(new OpenTextQuestionResponse().setOpenQuestion(openQuestion).setAnswer("answer"));

        responseRepository.save(surveyResponse);

        long respId = surveyResponse.getId();

        flushRepositories();

        Optional<SurveyResponse> byId = responseRepository.findById(respId);

        assertTrue(byId.isPresent());

    }

    @Test
    void testReceiveOpenTextQuestionResponse() {

        Survey survey = getSurvey();
        surveyRepository.save(survey);

        var campaign = new Campaign().setSurvey(survey);
        campaignRepository.save(campaign);

        OpenTextQuestion opentextQuestion = (OpenTextQuestion) survey.getSurveyPages().get(0)
                .getSurveyPageElements().get(0);

        var firstResponse = new SurveyResponse()
                .addElement(new OpenTextQuestionResponse().setOpenQuestion(opentextQuestion).setAnswer("first answer"))
                .setCampaign(campaign)
                .setSurvey(survey);
        var secondResponse = new SurveyResponse()
                .addElement(new OpenTextQuestionResponse().setOpenQuestion(opentextQuestion).setAnswer("second answer"))
                .setCampaign(campaign)
                .setSurvey(survey);

        responseRepository.saveAll(List.of(firstResponse, secondResponse));

        Page<OpenTextQuestionResponse> actualOpenTextResponses =
                responseRepository.findOpenTextQuestionResponseByCampaignId(campaign.getId(), Pageable.unpaged());

        assertEquals(2, actualOpenTextResponses.getTotalElements());
        assertEquals("first answer", actualOpenTextResponses.getContent().get(0).getAnswer());
        assertEquals("second answer", actualOpenTextResponses.getContent().get(1).getAnswer());


    }

    private Campaign getCampaign() {
        return new Campaign().setSurvey(getSurvey());
    }

    private Survey getSurvey() {

        var openQuestion = new OpenTextQuestion("This is an open question?");
        var closedQuestion = new ClosedQuestion("This is a closed question?")
                .addAnswer(new ClosedQuestionAnswer("first Answer"))
                .addAnswer(new ClosedQuestionAnswer("second Answer"));
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
