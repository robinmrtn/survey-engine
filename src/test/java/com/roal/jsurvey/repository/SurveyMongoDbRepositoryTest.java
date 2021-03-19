package com.roal.jsurvey.repository;


import com.roal.jsurvey.entity.ClosedQuestion;
import com.roal.jsurvey.entity.OpenQuestion;
import com.roal.jsurvey.entity.Survey;
import com.roal.jsurvey.entity.SurveyPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class SurveyMongoDbRepositoryTest {

    @Autowired
    private SurveyMongoDbRepository surveyRepository;

    @Test
    void insertSingleSurvey() {
        Survey survey = new Survey("This is a Survey");
        var firstSurveyPage = new SurveyPage();
        var openQuestion = new OpenQuestion("This is an open question?");
        var closedQuestion = new ClosedQuestion("This is a closed question?");
        firstSurveyPage.addSurveyElement(openQuestion);
        firstSurveyPage.addSurveyElement(closedQuestion);
        survey.addSurveyPage(firstSurveyPage);

        surveyRepository.save(survey);

        List<Survey> all = surveyRepository.findAll();

        Optional<Survey> byId = surveyRepository.findById(survey.getId());

        assertEquals(1, all.size());
        assertTrue(byId.isPresent());
        assertEquals(survey, byId.get());
        assertNotSame(survey, byId.get());


    }

}
