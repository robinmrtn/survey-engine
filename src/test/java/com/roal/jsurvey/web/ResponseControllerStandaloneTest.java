package com.roal.jsurvey.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roal.jsurvey.dto.ElementResponseDto;
import com.roal.jsurvey.dto.OpenQuestionResponseDto;
import com.roal.jsurvey.dto.SurveyResponseDto;
import com.roal.jsurvey.entity.questions.OpenQuestion;
import com.roal.jsurvey.entity.survey.Survey;
import com.roal.jsurvey.entity.survey.SurveyPage;
import com.roal.jsurvey.exception.SurveyExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
public class ResponseControllerStandaloneTest {

    private MockMvc mvc;

    @InjectMocks
    private ResponseController responseController;

    private JacksonTester<SurveyResponseDto> jsonSurveyResponseDto;
    private JacksonTester<ElementResponseDto> jsonElementResponseDto;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(responseController)
                .setControllerAdvice(new SurveyExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST response/survey/2 - success")
    void canCreateNewSurveyResponseForExistingSurvey() throws Exception {

        Survey survey = new Survey("This is a Survey");
        var firstSurveyPage = new SurveyPage();
        var openQuestion = new OpenQuestion(9, "This is an open question?");
        firstSurveyPage.addSurveyElement(openQuestion);
        survey.addSurveyPage(firstSurveyPage);

        List<ElementResponseDto> elementResponseDtos = List.of(new OpenQuestionResponseDto(9, "This is an answer!"));

        var responseDto = new SurveyResponseDto(elementResponseDtos);

        //given(surveyService.findSurveyById(2)).willReturn(survey);
        String json = jsonSurveyResponseDto.write(responseDto).getJson();
        MockHttpServletResponse response = mvc.perform(post("/response/survey/2")
                .contentType(MediaType.APPLICATION_JSON).content(jsonSurveyResponseDto.write(responseDto).getJson()))
                .andReturn().getResponse();
        var mapper = new ObjectMapper();
        json = mapper.writeValueAsString(responseDto);
        var deserialized = mapper.readValue(json, SurveyResponseDto.class);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    @DisplayName("POST response/survey/2 - failed")
    void canNotCreateNewSurveyResponseForNonExistingSurvey() throws Exception {

        // given(surveyService.findSurveyById(2)).willThrow(new SurveyNotFoundException());

        MockHttpServletResponse response = mvc.perform(post("/response/survey/2")
                .contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

}
