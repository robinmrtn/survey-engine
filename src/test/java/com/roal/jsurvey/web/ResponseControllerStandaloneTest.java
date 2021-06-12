package com.roal.jsurvey.web;

import com.roal.jsurvey.dto.ElementResponseDto;
import com.roal.jsurvey.dto.OpenQuestionResponseDto;
import com.roal.jsurvey.dto.SurveyResponseDto;
import com.roal.jsurvey.entity.questions.OpenTextQuestion;
import com.roal.jsurvey.entity.survey.Survey;
import com.roal.jsurvey.entity.survey.SurveyPage;
import com.roal.jsurvey.exception.SurveyNotFoundException;
import com.roal.jsurvey.service.ResponseService;
import com.roal.jsurvey.service.SurveyService;
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
    private SurveyService surveyService;

    @MockBean
    private ResponseService responseService;

    @Autowired
    private JacksonTester<SurveyResponseDto> jsonSurveyResponseDto;

    @Autowired
    private JacksonTester<ElementResponseDto> jsonElementResponseDto;

    @Test
    @DisplayName("POST response/survey/2 - success")
    void canCreateNewSurveyResponseForExistingSurvey() throws Exception {

        Survey survey = getSurvey();

        SurveyResponseDto responseDto = getSurveyResponseDto();

        given(surveyService.findSurveyById(2)).willReturn(survey);
        String json = jsonSurveyResponseDto.write(responseDto).getJson();
        MockHttpServletResponse response = mvc.perform(post("/responses/surveys/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonSurveyResponseDto.write(responseDto).getJson()))
                .andReturn().getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    @DisplayName("POST response/survey/2 - failed")
    void canNotCreateNewSurveyResponseForNonExistingSurvey() throws Exception {

        given(surveyService.findSurveyById(3)).willThrow(new SurveyNotFoundException());

        MockHttpServletResponse response = mvc.perform(post("/responses/surveys/3")
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

    private Survey getSurvey() {

        var openQuestion = new OpenTextQuestion(9, "This is an open question?");
        var firstSurveyPage = new SurveyPage()
                .addSurveyElement(openQuestion);

        Survey survey = new Survey("This is a Survey")
                .addSurveyPage(firstSurveyPage);
        return survey;
    }

}
