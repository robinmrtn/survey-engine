package com.roal.survey_engine.service;

import com.roal.survey_engine.response.dto.ElementResponseDto;
import com.roal.survey_engine.response.dto.OpenQuestionResponseDto;
import com.roal.survey_engine.response.dto.SurveyResponseDto;
import com.roal.survey_engine.survey.entity.Campaign;
import com.roal.survey_engine.survey.entity.Survey;
import com.roal.survey_engine.survey.exception.SurveyNotFoundException;
import com.roal.survey_engine.survey.repository.CampaignRepository;
import com.roal.survey_engine.survey.repository.SurveyRepository;
import com.roal.survey_engine.survey.service.SurveyService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SurveyServiceTest {

    @Autowired
    private SurveyService surveyService;

    @MockBean
    private SurveyRepository surveyRepository;

    @MockBean
    private CampaignRepository campaignRepository;

    @Test
    @DisplayName("Test findById success")
    void testFindByIdSuccess() {
        var survey = new Survey("This is a Survey");

        given(surveyRepository.findById(2L)).willReturn(Optional.of(survey));

        Survey returnedSurvey = surveyService.findSurveyById(2);

        assertSame(survey, returnedSurvey);

    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        var survey = Optional.of(new Survey("This is a Survey"));

        given(surveyRepository.findById(3L)).willReturn(Optional.empty());

        assertThrows(SurveyNotFoundException.class, () -> surveyService.findSurveyById(3));
    }

    @Test
    @Disabled
    @DisplayName("test insert Survey Response DTO")
    void testInsertSurveyResponseDto() {
        var surveyResponseDto = new SurveyResponseDto();
        List<ElementResponseDto> elementResponseDtos = new ArrayList<>();
        elementResponseDtos.add(new OpenQuestionResponseDto(9, "This is an answer"));
        surveyResponseDto.setElementResponseDtos(elementResponseDtos);
    }

    @Test
    void testGetPublicAndActiveSurveys() {
        given(campaignRepository.findByHiddenIsFalseAndActiveIsTrue()).willReturn(
                List.of(new Campaign().setSurvey(new Survey()),
                        new Campaign().setSurvey(new Survey()))
        );
        assertEquals(2, surveyService.getPublicAndActiveSurveys().size());
    }


}
