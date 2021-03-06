package com.roal.survey_engine.domain.survey;

import com.roal.survey_engine.domain.survey.dto.survey.SurveyListElementDto;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.exception.SurveyNotFoundException;
import com.roal.survey_engine.domain.survey.repository.SurveyRepository;
import com.roal.survey_engine.domain.survey.service.CampaignService;
import com.roal.survey_engine.domain.survey.service.SurveyService;
import com.roal.survey_engine.security.AuthenticationFacade;
import org.hashids.Hashids;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
@Transactional
class SurveyServiceTest {

    @Mock
    AuthenticationFacade authenticationFacade;
    @Mock
    CampaignService campaignService;
    @InjectMocks
    private SurveyService surveyService;
    @Mock
    private SurveyRepository surveyRepository;
    @Spy
    private Hashids surveyHashids = new Hashids();


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
    void testGetPublicAndActiveSurveys() {
        Page<SurveyListElementDto> surveyListElementDtos =
                new PageImpl<>(List.of(new SurveyListElementDto("aa", "survey1", "desc 1"),
                        new SurveyListElementDto("bb", "survey2", "desc 2")));

        given(campaignService.findPublicCampaigns(PageRequest.of(0, 10)))
                .willReturn(surveyListElementDtos);
        assertEquals(2, surveyService.getPublicSurveys(PageRequest.of(0, 10)).getTotalElements());
    }


}
