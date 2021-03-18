package com.roal.jsurvey.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roal.jsurvey.entity.OpenQuestion;
import com.roal.jsurvey.entity.Survey;
import com.roal.jsurvey.entity.SurveyPage;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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

        var expectedSurvey = new Survey("This is a survey");
        var firstSurveyPage = new SurveyPage();
        var openQuestion = new OpenQuestion("This is an open question");
        openQuestion.setPosition(1);
        firstSurveyPage.setPosition(1);
        firstSurveyPage.addSurveyElement(openQuestion);
        expectedSurvey.addSurveyPage(firstSurveyPage);

        given(surveyService.findSurveyById(2))
                .willReturn(Optional.of(expectedSurvey));

        MockHttpServletResponse response = mvc.perform(get("/survey/2")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonSurvey.write(expectedSurvey).getJson(), response.getContentAsString());
    }
}
