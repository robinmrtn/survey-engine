package com.roal.jsurvey.repository;

import com.roal.jsurvey.entity.questions.ClosedQuestion;
import com.roal.jsurvey.entity.questions.OpenQuestion;
import com.roal.jsurvey.entity.survey.Survey;
import com.roal.jsurvey.entity.survey.SurveyPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class SurveyJpaRepositoryTest {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("Test insert single Survey")
    void testInsertSingleSurvey() {
        Survey survey = new Survey("This is a Survey");
        var firstSurveyPage = new SurveyPage();
        var openQuestion = new OpenQuestion("This is an open question?");
        // var closedQuestion = new ClosedQuestion("This is a closed question?");

        firstSurveyPage.addSurveyElement(openQuestion);
        // firstSurveyPage.addSurveyElement(closedQuestion);

        survey.addSurveyPage(firstSurveyPage);

        surveyRepository.save(survey);

        testEntityManager.flush();
        // clears persistence context
        // all entities are now detached and can be fetched again
        testEntityManager.clear();

        var receivedSurvey = surveyRepository.findById(survey.getId());

        assertTrue(receivedSurvey.isPresent());

        assertEquals(survey, receivedSurvey.get());
        assertNotSame(survey, receivedSurvey.get());

        surveyRepository.deleteById(survey.getId());

        assertEquals(0, surveyRepository.findAll().size());

    }

    @Test
    void testGetAbstractElementByElementId() {
        Survey survey = new Survey("This is a Survey");
        var firstSurveyPage = new SurveyPage();
        var openQuestion = new OpenQuestion("This is an open question?");

        var closedQuestion = new ClosedQuestion("This is a closed question?");

        firstSurveyPage.addSurveyElement(openQuestion);
        firstSurveyPage.addSurveyElement(closedQuestion);

        survey.addSurveyPage(firstSurveyPage);

        surveyRepository.save(survey);

        testEntityManager.flush();
        // clears persistence context
        // all entities are now detached and can be fetched again
        testEntityManager.clear();


    }
}
