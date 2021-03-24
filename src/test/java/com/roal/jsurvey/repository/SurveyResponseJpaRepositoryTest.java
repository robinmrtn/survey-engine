package com.roal.jsurvey.repository;

import com.roal.jsurvey.entity.AbstractSurveyElement;
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

public class SurveyResponseJpaRepositoryTest {

    @Autowired
    ResponseRepository responseRepository;

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    TestEntityManager testEntityManager;


    @Test
    void testInsertResponse() {
        Survey survey = new Survey("This is a Survey");
        var firstSurveyPage = new SurveyPage();
        var openQuestion = new OpenQuestion("This is an open question?");

        var closedQuestion = new ClosedQuestion("This is a closed question?");

        firstSurveyPage.addSurveyElement(openQuestion);
        firstSurveyPage.addSurveyElement(closedQuestion);

        survey.addSurveyPage(firstSurveyPage);

        surveyRepository.save(survey);

        long surveyId = survey.getId();

        flushRepositories();

        var receivedSurvey = surveyRepository.getOne(surveyId);

        AbstractSurveyElement firstElement = receivedSurvey.getSurveyPages().get(0).getSurveyPageElement().get(0);
        AbstractSurveyElement secondElement = receivedSurvey.getSurveyPages().get(0).getSurveyPageElement().get(1);

        assertTrue(firstElement instanceof OpenQuestion);
        assertTrue(secondElement instanceof ClosedQuestion);

        var response = new SurveyResponse();
        response.setSurvey(receivedSurvey);
        var elementResponse1 = new OpenQuestionResponse();
        elementResponse1.setElement((OpenQuestion) firstElement);
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

    private void flushRepositories() {
        testEntityManager.flush();
        testEntityManager.clear();
    }
}
