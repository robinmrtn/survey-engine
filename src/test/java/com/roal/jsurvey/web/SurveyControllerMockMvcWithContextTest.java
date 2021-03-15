package com.roal.jsurvey.web;

import com.roal.jsurvey.entity.Survey;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureJsonTesters
@WebMvcTest(SurveyController.class)
public class SurveyControllerMockMvcWithContextTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SurveyService surveyService;

    @Autowired
    private JacksonTester<Survey> jsonSurvey;

    @Test
    @DisplayName("GET /survey/1 - success")
    public void canRetrieveByIWhenExists() throws Exception {
        given(surveyService.getSurveyById(1L))
                .willReturn(Optional.of(new Survey("This is a small survey")));

        MockHttpServletResponse response = mvc.perform(
                get("/survey/1")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonSurvey.write(new Survey("This is a small survey")).getJson(), response.getContentAsString());


    }

}
