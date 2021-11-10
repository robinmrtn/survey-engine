package com.roal.survey_engine.integration;

import com.roal.survey_engine.response.dto.ElementResponseDto;
import com.roal.survey_engine.response.dto.OpenQuestionResponseDto;
import com.roal.survey_engine.response.dto.SurveyResponseDto;
import com.roal.survey_engine.response.entity.SurveyResponse;
import com.roal.survey_engine.response.repository.ResponseRepository;
import com.roal.survey_engine.survey.entity.Campaign;
import com.roal.survey_engine.survey.entity.Survey;
import com.roal.survey_engine.survey.entity.SurveyPage;
import com.roal.survey_engine.survey.entity.question.AbstractSurveyElement;
import com.roal.survey_engine.survey.entity.question.OpenTextQuestion;
import com.roal.survey_engine.survey.repository.CampaignRepository;
import com.roal.survey_engine.survey.repository.SurveyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SurveyResponseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    ResponseRepository responseRepository;

    @Test
    void testPostResponseDto_Success() {

        Survey survey = createSurvey();
        Campaign campaign = createCampaign(survey);
        long campaignId = campaign.getId();
        long openQuestionId = getSurveyElementId(survey, OpenTextQuestion.class, 0);

        ResponseEntity<SurveyResponseDto> responseEntity =
                restTemplate.postForEntity("/api/responses/campaigns/" + campaignId,
                        createSurveyResponseDto(openQuestionId), SurveyResponseDto.class);

        List<SurveyResponse> responses = responseRepository.findAllByCampaignId(campaignId);

        assertAll(() -> assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode()),
                () -> assertFalse(responses.isEmpty()),
                () -> assertEquals(survey, responses.get(0).getSurvey()),
                () -> assertEquals(campaign, responses.get(0).getCampaign()));
    }

    @Test
    void testPostResponseDto_WrongFormat() {

        Survey survey = createSurvey();
        Campaign campaign = createCampaign(survey);
        long campaignId = campaign.getId();
        long openQuestionId = getSurveyElementId(survey, OpenTextQuestion.class, 0) + 1;

        ResponseEntity<SurveyResponseDto> responseEntity =
                restTemplate.postForEntity("/api/responses/campaigns/" + campaignId,
                        createSurveyResponseDto(openQuestionId), SurveyResponseDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testPostResponseDto_SurveyNoTFound() {

        Survey survey = createSurvey();
        Campaign campaign = createCampaign(survey);
        long campaignId = campaign.getId();
        long openQuestionId = getSurveyElementId(survey, OpenTextQuestion.class, 0);

        ResponseEntity<SurveyResponseDto> responseEntity =
                restTemplate.postForEntity("/api/responses/campaigns/" + campaignId + 1,
                        createSurveyResponseDto(openQuestionId), SurveyResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    private Campaign createCampaign(Survey survey) {
        var campaign = new Campaign()
                .setSurvey(survey);

        return campaignRepository.save(campaign);
    }

    private SurveyResponseDto createSurveyResponseDto(long openQuestionId) {

        var surveyResponseDto = new SurveyResponseDto();
        List<ElementResponseDto> elementDtoList = new ArrayList<>();
        elementDtoList.add(new OpenQuestionResponseDto(openQuestionId, "This is an answer to an open question."));
        surveyResponseDto.setElementResponseDtos(elementDtoList);

        return surveyResponseDto;
    }

    private Survey createSurvey() {

        var openQuestion = new OpenTextQuestion("This is an open question?")
                .setPosition(1);

        var surveyPage = new SurveyPage()
                .addSurveyElement(openQuestion);

        var survey = new Survey()
                .setDescription("This is a Survey")
                .addSurveyPage(surveyPage);

        return surveyRepository.save(survey);
    }

    private long getSurveyElementId(Survey survey, Class clazz, long skip) {
        return survey.getSurveyPages().stream()
                .flatMap(e -> e.getSurveyPageElements().stream())
                .filter(clazz::isInstance)
                .skip(skip)
                .mapToLong(AbstractSurveyElement::getId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(clazz.getName() + " was not found in " +
                        "Survey Elements."));
    }

}
