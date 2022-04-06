package com.roal.survey_engine.domain.survey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roal.survey_engine.domain.response.dto.ElementResponseDto;
import com.roal.survey_engine.domain.response.dto.SurveyResponseDto;
import com.roal.survey_engine.domain.survey.controller.SurveyController;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyDto;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyDtoMapper;
import com.roal.survey_engine.domain.survey.dto.survey.out.SurveyListElementDto;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.SurveyPage;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestion;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestionAnswer;
import com.roal.survey_engine.domain.survey.entity.question.OpenTextQuestion;
import com.roal.survey_engine.domain.survey.exception.SurveyExceptionHandler;
import com.roal.survey_engine.domain.survey.exception.SurveyNotFoundException;
import com.roal.survey_engine.domain.survey.service.SurveyService;
import org.hashids.Hashids;
import org.junit.jupiter.api.BeforeAll;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
class SurveyControllerStandaloneTest {

    private MockMvc mvc;

    @Mock
    private SurveyService surveyService;

    private final SurveyDtoMapper surveyDtoMapper = new SurveyDtoMapper(new Hashids());

    @InjectMocks
    private SurveyController surveyController;

    // This object will be magically initialized by the initFields method below.
    private JacksonTester<SurveyDto> jsonSurvey;
    private JacksonTester<List> jsonList;


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
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new SurveyExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /surveys/2 - success")
    void canRetrieveByIWhenExists() throws Exception {

        given(surveyService.findSurveyByCampaignId("abcd123"))
                .willReturn(surveyDtoMapper.entityToDto(survey));

        MockHttpServletResponse response = mvc.perform(
                        get("/api/surveys/abcd123")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(() -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(jsonSurvey.write(surveyDtoMapper.entityToDto(survey)).getJson(),
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
                () -> assertTrue(response.getContentAsString().isEmpty()));
    }

    @Test
    @DisplayName("GET /surveys/2 - success with Page and OpenQuestion")
    void canRetrieveSurveyWithPageAndOpenQuestion() throws Exception {

        given(surveyService.findSurveyByCampaignId("abcd123"))
                .willReturn(surveyDtoMapper.entityToDto(survey));

        MockHttpServletResponse response = mvc.perform(get("/api/surveys/abcd123")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(() -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(jsonSurvey.write(surveyDtoMapper.entityToDto(survey)).getJson(), response.getContentAsString()));

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
}
