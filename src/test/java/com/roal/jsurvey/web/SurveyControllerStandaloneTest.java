package com.roal.jsurvey.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roal.jsurvey.dto.ElementResponseDto;
import com.roal.jsurvey.dto.SurveyResponseDto;
import com.roal.jsurvey.entity.questions.ClosedQuestion;
import com.roal.jsurvey.entity.questions.OpenTextQuestion;
import com.roal.jsurvey.entity.survey.Survey;
import com.roal.jsurvey.entity.survey.SurveyPage;
import com.roal.jsurvey.exception.SurveyExceptionHandler;
import com.roal.jsurvey.exception.SurveyNotFoundException;
import com.roal.jsurvey.service.SurveyService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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


    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(surveyController)
                .setControllerAdvice(new SurveyExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /surveys/2 - success")
    void canRetrieveByIWhenExists() throws Exception {

        given(surveyService.findSurveyById(2L))
                .willReturn(new Survey("This is a small survey"));

        MockHttpServletResponse response = mvc.perform(
                get("/surveys/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonSurvey.write(new Survey("This is a small survey")).getJson(),
                response.getContentAsString());
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

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());

    }

    @Test
    @DisplayName("GET /surveys/2 - success with Page and OpenQuestion")
    void canRetrieveSurveyWithPageAndOpenQuestion() throws Exception {

        var closedQuestion = new ClosedQuestion("This is a closed question?");
        var openQuestion = new OpenTextQuestion("This is an open question?");
        var firstSurveyPage = new SurveyPage()
                .addSurveyElement(openQuestion)
                .addSurveyElement(closedQuestion);

        Survey survey = new Survey("This is a Survey")
                .addSurveyPage(firstSurveyPage);

        given(surveyService.findSurveyById(2))
                .willReturn(survey);

        MockHttpServletResponse response = mvc.perform(get("/surveys/2")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonSurvey.write(survey).getJson(), response.getContentAsString());
    }

}
