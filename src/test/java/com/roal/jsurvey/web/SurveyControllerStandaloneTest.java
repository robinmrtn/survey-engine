package com.roal.jsurvey.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roal.jsurvey.entity.*;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
public class SurveyControllerStandaloneTest {

    private MockMvc mvc;

    @Mock
    private SurveyService surveyService;

    @InjectMocks
    private SurveyController surveyController;

    // This object will be magically initialized by the initFields method below.
    private JacksonTester<Survey> jsonSurvey;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(surveyController)
                .setControllerAdvice(new SurveyExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /survey/2 - success")
    public void canRetrieveByIWhenExists() throws Exception {

        given(surveyService.findSurveyById(2L))
                .willReturn(Optional.of(new Survey("This is a small survey")));

        MockHttpServletResponse response = mvc.perform(
                get("/survey/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonSurvey.write(new Survey("This is a small survey")).getJson(), response.getContentAsString());
    }

    @Test
    @DisplayName("GET /survey/2 - 404")
    public void cannotRetrieveByIdWhenNotExists() throws Exception {

        given(surveyService.findSurveyById(2L))
                .willThrow(new SurveyNotFoundException());

        MockHttpServletResponse response = mvc.perform(
                get("/survey/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());

    }

    @Test
    @DisplayName("GET /survey/2 - success with Page and OpenQuestion")
    public void canRetrieveSurveyWithPageAndOpenQuestion() throws Exception {

        Survey survey = new Survey("This is a Survey");
        var firstSurveyPage = new SurveyPage();
        var openQuestion = new OpenQuestion("This is an open question?");
        var closedQuestion = new ClosedQuestion("This is a closed question?");
        // var opqPosition = new SurveyPagePosition(1, openQuestion);
        var clqPosition = new SurveyPagePosition(2, closedQuestion);
        // firstSurveyPage.addSurveyElement(opqPosition);
        firstSurveyPage.addSurveyElement(clqPosition);

        survey.addSurveyPage(firstSurveyPage);

        given(surveyService.findSurveyById(2))
                .willReturn(Optional.of(survey));

        MockHttpServletResponse response = mvc.perform(get("/survey/2")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonSurvey.write(survey).getJson(), response.getContentAsString());
    }

    @Test
    @DisplayName("POST /survey/2 - success")
    void canCreateNewSurveyResponseForExistingSurvey() throws Exception {

        Survey survey = new Survey("This is a Survey");
        var firstSurveyPage = new SurveyPage();
        var openQuestion = new OpenQuestion(9, "This is an open question?");
        var opqPosition = new SurveyPagePosition(1, openQuestion);

        firstSurveyPage.addSurveyElement(opqPosition);
        survey.addSurveyPage(firstSurveyPage);

        given(surveyService.findSurveyById(2)).willReturn(Optional.of(survey));

        MockHttpServletResponse response = mvc.perform(post("/survey/2")
                .contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn().getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    @DisplayName("POST /survey/2 - failed")
    void canNotCreateNewSurveyResponseForNonExistingSurvey() throws Exception {

        given(surveyService.findSurveyById(2)).willThrow(new SurveyNotFoundException());

        MockHttpServletResponse response = mvc.perform(post("/survey/2")
                .contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }
}
