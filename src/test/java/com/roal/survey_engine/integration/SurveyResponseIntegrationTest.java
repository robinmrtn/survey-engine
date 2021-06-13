package com.roal.survey_engine.integration;

import com.roal.survey_engine.dto.ElementResponseDto;
import com.roal.survey_engine.dto.OpenQuestionResponseDto;
import com.roal.survey_engine.dto.SurveyResponseDto;
import com.roal.survey_engine.entity.questions.AbstractSurveyElement;
import com.roal.survey_engine.entity.questions.OpenTextQuestion;
import com.roal.survey_engine.entity.survey.Campaign;
import com.roal.survey_engine.entity.survey.Survey;
import com.roal.survey_engine.entity.survey.SurveyPage;
import com.roal.survey_engine.repository.CampaignRepository;
import com.roal.survey_engine.repository.SurveyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SurveyResponseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Test
    void testPostResponseDto_Success() {

        Survey survey = createSurvey();
        Campaign campaign = createCompaign(survey);
        long campaignId = campaign.getId();
        long openQuestionId = getSurveyElementId(survey, OpenTextQuestion.class, 0);

        ResponseEntity<SurveyResponseDto> responseEntity =
                restTemplate.postForEntity("/responses/campaigns/" + campaignId,
                        createSurveyResponseDto(openQuestionId), SurveyResponseDto.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testPostResponseDto_WrongFormat() {

        Survey survey = createSurvey();
        Campaign campaign = createCompaign(survey);
        long campaignId = campaign.getId();
        long openQuestionId = getSurveyElementId(survey, OpenTextQuestion.class, 0) + 1;

        ResponseEntity<SurveyResponseDto> responseEntity =
                restTemplate.postForEntity("/responses/campaigns/" + campaignId,
                        createSurveyResponseDto(openQuestionId), SurveyResponseDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testPostResponseDto_SurveyNoTFound() {

        Survey survey = createSurvey();
        Campaign campaign = createCompaign(survey);
        long campaignId = campaign.getId();
        long openQuestionId = getSurveyElementId(survey, OpenTextQuestion.class, 0);

        ResponseEntity<SurveyResponseDto> responseEntity =
                restTemplate.postForEntity("/responses/campaigns/" + campaignId + 1,
                        createSurveyResponseDto(openQuestionId), SurveyResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    private Campaign createCompaign(Survey survey) {
        var campaign = new Campaign()
                .setSurvey(survey);
        campaignRepository.save(campaign);
        return campaign;
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
