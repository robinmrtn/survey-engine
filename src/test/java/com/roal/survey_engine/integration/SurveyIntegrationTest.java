package com.roal.survey_engine.integration;

//import com.roal.survey_engine.dto.survey.SurveyDto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyDto;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyDtoMapper;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.SurveyPage;
import com.roal.survey_engine.domain.survey.entity.Workspace;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestion;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestionAnswer;
import com.roal.survey_engine.domain.survey.entity.question.OpenTextQuestion;
import com.roal.survey_engine.domain.survey.repository.CampaignRepository;
import com.roal.survey_engine.domain.survey.repository.SurveyRepository;
import com.roal.survey_engine.domain.survey.repository.WorkspaceRepository;
import com.roal.survey_engine.domain.user.UserAuthority;
import com.roal.survey_engine.security.jwt.TokenProvider;
import org.hashids.Hashids;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles({"test"})
@Transactional
@Sql({"/survey_json.sql"})
public class SurveyIntegrationTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    SurveyDtoMapper mapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    @Qualifier("surveyHashids")
    Hashids surveyHashids;

    @Autowired
    @Qualifier("workspaceHashids")
    Hashids workspaceHashids;

    @Autowired
    private TokenProvider tokenProvider;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void getSurvey() throws Exception {

        Campaign campaign = createCampaign();
        String id = surveyHashids.encode(campaign.getId());
        mvc.perform(get("/api/surveys/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getSurveys() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/api/surveys")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @DisplayName("should return 201 when new Survey is submitted")
    void postNewSurvey() throws Exception {

        String username = "user";
        String token = tokenProvider.generateToken(username);

        given(userDetailsService.loadUserByUsername("user")).willReturn(new User("user", "password",
                Set.of(new SimpleGrantedAuthority(UserAuthority.Constants.ADMIN_VALUE))));

        Long id = createWorkspace().getId();
        String hashid = workspaceHashids.encode(id);
        SurveyDto surveyDto = mapper.entityToDto(createSurvey());
        String json = objectMapper.writeValueAsString(surveyDto);
        mvc.perform(post("/api/workspaces/" + hashid + "/surveys/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(json))
                .andExpect(status().isCreated());
    }

    private Workspace createWorkspace() {
        Workspace workspace = new Workspace("workspace");
        workspaceRepository.saveAndFlush(workspace);
        return workspace;
    }

    private Survey createSurvey() {

        return new Survey("This is a survey")
                .setTitle("Title")
                .addSurveyPage(new SurveyPage()
                        .addSurveyElement(new OpenTextQuestion("This is a closed question")))
                .addSurveyPage(new SurveyPage()
                        .addSurveyElement(new ClosedQuestion("This is a closed question")
                                .addAnswer(new ClosedQuestionAnswer("answer1"))
                                .addAnswer(new ClosedQuestionAnswer("answer2"))))

                .setWorkspace(createWorkspace());
    }

    private Campaign createCampaign() {

        var openQuestion = new OpenTextQuestion("This is an open question?")
                .setPosition(1);

        var surveyPage = new SurveyPage()
                .addSurveyElement(openQuestion);

        var survey = new Survey()
                .setDescription("This is a Survey")
                .addSurveyPage(surveyPage)
                .setWorkspace(createWorkspace());

        var campaign = new Campaign().setSurvey(survey);
        surveyRepository.saveAndFlush(survey);
        return campaignRepository.saveAndFlush(campaign);
    }

}
