package com.roal.survey_engine.domain.response;

import com.roal.survey_engine.domain.response.controller.ResponseController;
import com.roal.survey_engine.domain.response.dto.ElementResponseDto;
import com.roal.survey_engine.domain.response.dto.OpenNumericQuestionResponseDto;
import com.roal.survey_engine.domain.response.dto.OpenQuestionResponseDto;
import com.roal.survey_engine.domain.response.dto.SurveyResponseDto;
import com.roal.survey_engine.domain.response.service.ResponseService;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.SurveyPage;
import com.roal.survey_engine.domain.survey.entity.question.OpenNumericQuestion;
import com.roal.survey_engine.domain.survey.entity.question.OpenTextQuestion;
import com.roal.survey_engine.domain.survey.exception.SurveyNotFoundException;
import com.roal.survey_engine.domain.survey.service.CampaignService;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
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

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private JacksonTester<SurveyResponseDto> jsonSurveyResponseDto;

    @Autowired
    private JacksonTester<ElementResponseDto> jsonElementResponseDto;

    @Test
    @DisplayName("POST responses/surveys/{id} - success")
    void canCreateNewSurveyResponseForExistingSurvey() throws Exception {

        Campaign campaign = getCampaign();

        SurveyResponseDto responseDto = getSurveyResponseDto();

        given(campaignService.findCampaignById(2)).willReturn(campaign);
        String json = jsonSurveyResponseDto.write(responseDto).getJson();
        MockHttpServletResponse response = mvc.perform(post("/api/responses/campaigns/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSurveyResponseDto.write(responseDto).getJson()))
                .andReturn().getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    @DisplayName("POST responses/surveys/{id} - failed")
    @WithMockUser
    void canNotCreateNewSurveyResponseForNonExistingSurvey() throws Exception {

        given(campaignService.findCampaignById("abcd123")).willThrow(new SurveyNotFoundException());

        MockHttpServletResponse response = mvc.perform(post("/api/responses/surveys/abcd123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSurveyResponseDto.write(getSurveyResponseDto()).getJson()))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    private SurveyResponseDto getSurveyResponseDto() {
        List<ElementResponseDto> elementResponseDtos =
                List.of(new OpenQuestionResponseDto(9, "This is an answer!"),
                        new OpenNumericQuestionResponseDto(14, 3.0));

        return new SurveyResponseDto(elementResponseDtos);
    }

    private Campaign getCampaign() {

        var openQuestion = new OpenTextQuestion(9, "This is an open question?");
        var openNumericQuestion = new OpenNumericQuestion(14, "This is an open numeric question");
        var firstSurveyPage = new SurveyPage()
                .addSurveyElement(openQuestion)
                .addSurveyElement(openNumericQuestion);

        Survey survey = new Survey("This is a Survey")
                .addSurveyPage(firstSurveyPage);
        return new Campaign().setSurvey(survey);
    }

}
