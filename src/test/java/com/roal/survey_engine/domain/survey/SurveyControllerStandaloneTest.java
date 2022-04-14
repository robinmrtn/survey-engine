package com.roal.survey_engine.domain.survey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roal.survey_engine.domain.survey.controller.SurveyController;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyDto;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyPageDto;
import com.roal.survey_engine.domain.survey.dto.survey.element.OpenQuestionDto;
import com.roal.survey_engine.domain.survey.dto.survey.out.SurveyListElementDto;
import com.roal.survey_engine.domain.survey.exception.SurveyExceptionHandler;
import com.roal.survey_engine.domain.survey.exception.SurveyNotFoundException;
import com.roal.survey_engine.domain.survey.service.SurveyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private JacksonTester<SurveyDto> jsonSurvey;


    @BeforeEach
    public void init() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(surveyController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new SurveyExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /surveys/2 - success")
    void canRetrieveByIWhenExists() throws Exception {

        given(surveyService.findSurveyByCampaignId("abcd123"))
                .willReturn(getSurveyDto());

        MockHttpServletResponse response = mvc.perform(
                        get("/api/surveys/abcd123")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(() -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(jsonSurvey.write(getSurveyDto()).getJson(),
                        response.getContentAsString()));
    }

    @Test
    @DisplayName("GET /surveys/2 - 404")
    void cannotRetrieveByIdWhenNotExists() throws Exception {

        given(surveyService.findSurveyByCampaignId("abcd123"))
                .willThrow(new SurveyNotFoundException());

        MockHttpServletResponse response = mvc.perform(
                        get("/api/surveys/abcd123")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(() -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Survey not found", response.getContentAsString()));
    }


    @Test
    void canRetrieveSurveyList() throws Exception {
        List<SurveyListElementDto> surveys = List.of(
                new SurveyListElementDto("aa", "First name", "First desc"),
                new SurveyListElementDto("bb", "second Name", "second Desc"));
        Page<SurveyListElementDto> surveyPage = new PageImpl<>(surveys, PageRequest.of(0, 10), 2);

        given(surveyService.getPublicSurveys(PageRequest.of(0, 10)))
                .willReturn(surveyPage);


        MockHttpServletResponse response = mvc.perform(get("/api/surveys")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(() -> assertEquals(HttpStatus.OK.value(), response.getStatus()));

    }

    private SurveyDto getSurveyDto() {
        return new SurveyDto("abcd123", "Title", "description", "bbbbb",
                List.of(new SurveyPageDto(1, List.of(new OpenQuestionDto("question", 1, 99)))));
    }
}
