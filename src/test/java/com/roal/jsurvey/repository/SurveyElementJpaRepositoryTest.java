package com.roal.jsurvey.repository;

import com.roal.jsurvey.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class SurveyElementJpaRepositoryTest {

    @Autowired
    private SurveyElementRepository surveyElementRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void testGetElement() {

        Survey survey = new Survey("This is a Survey");
        var firstSurveyPage = new SurveyPage();
        var openQuestion = new OpenQuestion("This is an open question?");
        //openQuestion.setId(99);
        var closedQuestion = new ClosedQuestion("This is a closed question?");
        //closedQuestion.setId(101);
        var opqPosition = new SurveyPagePosition(2, openQuestion);
        var clqPosition = new SurveyPagePosition(1, closedQuestion);
        firstSurveyPage.addSurveyElement(opqPosition);
        firstSurveyPage.addSurveyElement(clqPosition);

        survey.addSurveyPage(firstSurveyPage);

        surveyRepository.save(survey);

        long opqId = survey.getSurveyPages().get(0).getSurveyPagePositions().get(0).getSurveyElement().getId();
        long clqId = survey.getSurveyPages().get(0).getSurveyPagePositions().get(1).getSurveyElement().getId();

        testEntityManager.flush();
        // clears persistence context
        // all entities are now detached and can be fetched again
        testEntityManager.clear();

        AbstractSurveyElement openQuestionFromRepo = surveyElementRepository.getOne(opqId);
        AbstractSurveyElement closedQuestionFromRepo = surveyElementRepository.getOne(clqId);

        assertNotNull(openQuestionFromRepo);
        assertNotNull(closedQuestionFromRepo);
        assertEquals(closedQuestion.getId(), closedQuestionFromRepo.getId());
        assertEquals(openQuestion.getId(), openQuestionFromRepo.getId());


    }
}
