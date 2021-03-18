package com.roal.jsurvey.service;

import com.roal.jsurvey.entity.Survey;
import com.roal.jsurvey.repository.SurveyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SurveyServiceTest {

    @Autowired
    private SurveyService surveyService;

    @MockBean
    private SurveyRepository surveyRepository;

    @Test
    @DisplayName("Test findById success")
    void testFindByIdSuccess() {
        var survey = new Survey("This is a Survey");

        given(surveyRepository.findById(2)).willReturn(Optional.of(survey));

        Optional<Survey> returnedSurvey = surveyService.findSurveyById(2);

        assertTrue(returnedSurvey.isPresent());
        assertSame(returnedSurvey.get(), survey);

    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        var survey = Optional.of(new Survey("This is a Survey"));

        given(surveyRepository.findById(3)).willReturn(Optional.empty());

        Optional<Survey> returnedSurvey = surveyService.findSurveyById(3);

        assertFalse(returnedSurvey.isPresent());

    }
}
