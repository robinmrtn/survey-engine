package com.roal.survey_engine.web;

import com.roal.survey_engine.dto.response.ElementResponseDto;
import com.roal.survey_engine.dto.response.OpenQuestionResponseDto;
import com.roal.survey_engine.dto.response.SurveyResponseDto;
import com.roal.survey_engine.entity.question.OpenTextQuestion;
import com.roal.survey_engine.entity.survey.Campaign;
import com.roal.survey_engine.entity.survey.Survey;
import com.roal.survey_engine.entity.survey.SurveyPage;
import com.roal.survey_engine.exception.SurveyNotFoundException;
import com.roal.survey_engine.service.CampaignService;
import com.roal.survey_engine.service.ResponseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureJsonTesters
@WebMvcTest(ResponseController.class)
class ResponseControllerStandaloneTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ResponseService responseService;

    @MockBean
    private CampaignService campaignService;

    @Autowired
    private JacksonTester<SurveyResponseDto> jsonSurveyResponseDto;

    @Autowired
    private JacksonTester<ElementResponseDto> jsonElementResponseDto;

    @Test
    @DisplayName("POST responses/campaigns/{id} - success")
    void canCreateNewSurveyResponseForExistingSurvey() throws Exception {

        Campaign campaign = getCampaign();

        SurveyResponseDto responseDto = getSurveyResponseDto();

        given(campaignService.findCampaignById(2)).willReturn(campaign);
        String json = jsonSurveyResponseDto.write(responseDto).getJson();
        MockHttpServletResponse response = mvc.perform(post("/responses/campaigns/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonSurveyResponseDto.write(responseDto).getJson()))
                .andReturn().getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    @DisplayName("POST responses/campaigns/{id} - failed")
    void canNotCreateNewSurveyResponseForNonExistingSurvey() throws Exception {

        given(campaignService.findCampaignById(3)).willThrow(new SurveyNotFoundException());

        MockHttpServletResponse response = mvc.perform(post("/responses/campaigns/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonSurveyResponseDto.write(getSurveyResponseDto()).getJson()))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    private SurveyResponseDto getSurveyResponseDto() {
        List<ElementResponseDto> elementResponseDtos =
                List.of(new OpenQuestionResponseDto(9, "This is an answer!"));

        return new SurveyResponseDto(elementResponseDtos);
    }

    private Campaign getCampaign() {

        var openQuestion = new OpenTextQuestion(9, "This is an open question?");
        var firstSurveyPage = new SurveyPage()
                .addSurveyElement(openQuestion);

        Survey survey = new Survey("This is a Survey")
                .addSurveyPage(firstSurveyPage);
        return new Campaign().setSurvey(survey);
    }

}
