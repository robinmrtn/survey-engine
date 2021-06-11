package com.roal.jsurvey.repository;

import com.roal.jsurvey.entity.questions.ClosedQuestion;
import com.roal.jsurvey.entity.questions.OpenQuestion;
import com.roal.jsurvey.entity.responses.OpenQuestionResponse;
import com.roal.jsurvey.entity.responses.SurveyResponse;
import com.roal.jsurvey.entity.survey.Survey;
import com.roal.jsurvey.entity.survey.SurveyPage;
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
        var elementResponse1 = new OpenQuestionResponse();
        elementResponse1.setValue("This is an answer!");
        response.setElementResponses(List.of(elementResponse1));

        responseRepository.save(response);

        long responseId = response.getId();

        flushRepositories();

        Optional<SurveyResponse> byId = responseRepository.findById(responseId);

        assertTrue(byId.isPresent());
        assertEquals(response, byId.get());
        assertNotSame(response, byId.get());

    }

    private Survey getSurvey() {

        var openQuestion = new OpenQuestion("This is an open question?");
        var closedQuestion = new ClosedQuestion("This is a closed question?");
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
