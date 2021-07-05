package com.roal.survey_engine.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roal.survey_engine.dto.response.ElementResponseDto;
import com.roal.survey_engine.dto.response.SurveyResponseDto;
import com.roal.survey_engine.entity.question.ClosedQuestion;
import com.roal.survey_engine.entity.question.ClosedQuestionAnswer;
import com.roal.survey_engine.entity.question.OpenTextQuestion;
import com.roal.survey_engine.entity.survey.Survey;
import com.roal.survey_engine.entity.survey.SurveyPage;
import com.roal.survey_engine.exception.SurveyExceptionHandler;
import com.roal.survey_engine.exception.SurveyNotFoundException;
import com.roal.survey_engine.service.SurveyService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
class SurveyControllerStandaloneTest {

    private MockMvc mvc;

    @Mock
    private SurveyService surveyService;

    @InjectMocks
    private SurveyController surveyController;

    // This object will be magically initialized by the initFields method below.
    private JacksonTester<Survey> jsonSurvey;

    private JacksonTester<SurveyResponseDto> jsonSurveyResponseDto;
    private JacksonTester<ElementResponseDto> jsonElementResponseDto;

    private static Survey survey;

    @BeforeAll
    public static void setup() {
        var openQuestion = new OpenTextQuestion("This is an open question?");
        var closedQuestion = new ClosedQuestion("This is a closed question?")
                .addAnswer(new ClosedQuestionAnswer("first answer"))
                .addAnswer(new ClosedQuestionAnswer("second answer"));

        survey = new Survey("This is a Survey")
                .addSurveyPage(new SurveyPage().addSurveyElement(openQuestion))
                .addSurveyPage(new SurveyPage().addSurveyElement(closedQuestion));
    }

    @BeforeEach
    public void init() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(surveyController)
                .setControllerAdvice(new SurveyExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /surveys/2 - success")
    void canRetrieveByIWhenExists() throws Exception {

        given(surveyService.findSurveyById(2L))
                .willReturn(survey);

        MockHttpServletResponse response = mvc.perform(
                get("/surveys/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(() -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(jsonSurvey.write(survey).getJson(),
                        response.getContentAsString()));
    }

    @Test
    @DisplayName("GET /surveys/2 - 404")
    void cannotRetrieveByIdWhenNotExists() throws Exception {

        given(surveyService.findSurveyById(2L))
                .willThrow(new SurveyNotFoundException());

        MockHttpServletResponse response = mvc.perform(
                get("/surveys/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(() -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertTrue(response.getContentAsString().isEmpty()));
    }

    @Test
    @DisplayName("GET /surveys/2 - success with Page and OpenQuestion")
    void canRetrieveSurveyWithPageAndOpenQuestion() throws Exception {

        given(surveyService.findSurveyById(2))
                .willReturn(survey);

        MockHttpServletResponse response = mvc.perform(get("/surveys/2")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(() -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(jsonSurvey.write(survey).getJson(), response.getContentAsString()));

    }

}
