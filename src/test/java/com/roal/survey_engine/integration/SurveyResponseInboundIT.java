package com.roal.survey_engine.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roal.survey_engine.domain.response.dto.*;
import com.roal.survey_engine.domain.response.repository.ResponseRepository;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.SurveyPage;
import com.roal.survey_engine.domain.survey.entity.question.*;
import com.roal.survey_engine.domain.survey.repository.CampaignRepository;
import com.roal.survey_engine.domain.survey.repository.SurveyRepository;
import org.hashids.Hashids;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SurveyResponseInboundIT {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    @Qualifier("campaignHashids")
    private Hashids campaignHashids;

    @BeforeEach
    void init() {
        responseRepository.deleteAll();
        campaignRepository.deleteAll();
        surveyRepository.deleteAll();
    }

    @Test
    void testPostResponseDto_Success() throws Exception {

        Survey survey = createSurvey();
        Campaign campaign = createCampaign(survey);
        String id = campaignHashids.encode(campaign.getId());

        String json = objectMapper.writeValueAsString(createSurveyResponseDto(survey));

        mvc.perform(post("/api/responses/campaigns/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.elementResponseDtos", hasSize(3)));
    }

    @Test
    void testPostResponseDto_WrongFormat() throws Exception {

        Survey survey = createSurvey();
        Campaign campaign = createCampaign(survey);
        String id = campaignHashids.encode(campaign.getId());

        String json = objectMapper.writeValueAsString(makeDtoInvalid(createSurveyResponseDto(survey)));

        mvc.perform(post("/api/responses/campaigns/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void testPostResponseDto_SurveyNoTFound() throws Exception {

        Survey survey = createSurvey();
        Campaign campaign = createCampaign(survey);
        SurveyResponseDto dto = createSurveyResponseDto(survey);

        String json = objectMapper.writeValueAsString(dto);

        mvc.perform(post("/api/responses/campaigns/abcd")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    private SurveyResponseDto createSurveyResponseDto(Survey survey) {

        List<AbstractSurveyElement> surveyElements = survey.getSurveyPages().stream()
                .flatMap(e -> e.getSurveyPageElements().stream())
                .collect(Collectors.toList());

        var surveyResponseDto = new SurveyResponseDto();
        List<ElementResponseDto> elementResponseDtos = new ArrayList<>();

        for (AbstractSurveyElement surveyElement : surveyElements) {
            if (surveyElement instanceof OpenTextQuestion openTextQuestion) {
                elementResponseDtos.add(new OpenQuestionResponseDto(openTextQuestion.getId(),
                        "This is an answer to an open text question"));
            } else if (surveyElement instanceof OpenNumericQuestion openNumericQuestion) {
                elementResponseDtos.add(new OpenNumericQuestionResponseDto(openNumericQuestion.getId(), 4.0));
            } else if (surveyElement instanceof ClosedQuestion closedQuestion) {
                long answerId = closedQuestion.getAnswers().get(0).getId();
                elementResponseDtos.add(new ClosedQuestionResponseDto(closedQuestion.getId(), Set.of(answerId)));
            }
        }
        surveyResponseDto.setElementResponseDtos(elementResponseDtos);

        return surveyResponseDto;
    }

    private SurveyResponseDto makeDtoInvalid(SurveyResponseDto surveyResponseDto) {
        surveyResponseDto.getElementResponseDtos().forEach(e -> e.setElementId(-1));
        return surveyResponseDto;
    }

    private Campaign createCampaign(Survey survey) {
        var campaign = new Campaign()
                .setSurvey(survey);

        return campaignRepository.save(campaign);
    }

    private Survey createSurvey() {

        var openQuestion = new OpenTextQuestion("This is an open question?")
                .setPosition(1);

        var closedQuestion = new ClosedQuestion("This is an closed question")
                .addAnswer(new ClosedQuestionAnswer("first answer"))
                .addAnswer(new ClosedQuestionAnswer("second answer"));

        var openNumericQuestion = new OpenNumericQuestion("THis is a numeric question");

        var surveyPage = new SurveyPage()
                .addSurveyElement(openQuestion)
                .addSurveyElement(closedQuestion)
                .addSurveyElement(openNumericQuestion);

        var survey = new Survey()
                .setDescription("This is a Survey")
                .addSurveyPage(surveyPage);

        return surveyRepository.save(survey);
    }
}
