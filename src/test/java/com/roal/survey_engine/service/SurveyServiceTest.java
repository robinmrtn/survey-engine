package com.roal.survey_engine.service;

import com.roal.survey_engine.dto.response.ElementResponseDto;
import com.roal.survey_engine.dto.response.OpenQuestionResponseDto;
import com.roal.survey_engine.dto.response.SurveyResponseDto;
import com.roal.survey_engine.entity.survey.Survey;
import com.roal.survey_engine.exception.SurveyNotFoundException;
import com.roal.survey_engine.repository.SurveyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class SurveyServiceTest {

    @Autowired
    private SurveyService surveyService;

    @MockBean
    private SurveyRepository surveyRepository;

    @Test
    @DisplayName("Test findById success")
    void testFindByIdSuccess() {
        var survey = new Survey("This is a Survey");

        given(surveyRepository.findById(2)).willReturn(Optional.of(survey));

        Survey returnedSurvey = surveyService.findSurveyById(2);

        assertSame(survey, returnedSurvey);

    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        var survey = Optional.of(new Survey("This is a Survey"));

        given(surveyRepository.findById(3)).willReturn(Optional.empty());

        assertThrows(SurveyNotFoundException.class, () -> surveyService.findSurveyById(3));
    }

    @Test
    @DisplayName("test insert Survey Response DTO")
    void testInsertSurveyResponseDto() {

        var surveyResponseDto = new SurveyResponseDto();
        List<ElementResponseDto> elementResponseDtos = new ArrayList<>();
        elementResponseDtos.add(new OpenQuestionResponseDto(9, "This is an answer"));
        surveyResponseDto.setElementResponseDtos(elementResponseDtos);

    }


}
