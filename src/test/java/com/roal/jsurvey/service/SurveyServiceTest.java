package com.roal.jsurvey.service;

import com.roal.jsurvey.entity.Survey;
import com.roal.jsurvey.repository.SurveyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
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



}
