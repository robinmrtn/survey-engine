package com.roal.jsurvey.repository;

import com.roal.jsurvey.entity.ClosedQuestion;
import com.roal.jsurvey.entity.OpenQuestion;
import com.roal.jsurvey.entity.Survey;
import com.roal.jsurvey.entity.SurveyPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class SurveyJpaRepositoryTest {

    @Autowired
    private SurveyRepository surveyRepository;

    @Test
    @DisplayName("Test insert single Survey")
    void testInsertSingleSurvey() {
        Survey survey = new Survey("This is a Survey");
        var firstSurveyPage = new SurveyPage();
        var openQuestion = new OpenQuestion("This is an open question?");
       var closedQuestion = new ClosedQuestion("This is a closed question?");
        firstSurveyPage.addSurveyElement(openQuestion);
        firstSurveyPage.addSurveyElement(closedQuestion);
        survey.addSurveyPage(firstSurveyPage);

        surveyRepository.save(survey);

        assertEquals(1, surveyRepository.findAll().size());
        assertEquals(survey, surveyRepository.findAll().get(0));

        surveyRepository.deleteById(survey.getId());

        assertEquals(0, surveyRepository.findAll().size());

    }

}
