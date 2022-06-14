package com.roal.survey_engine.runner;

import com.roal.survey_engine.domain.response.entity.ClosedQuestionResponse;
import com.roal.survey_engine.domain.response.entity.OpenNumericQuestionResponse;
import com.roal.survey_engine.domain.response.entity.OpenTextQuestionResponse;
import com.roal.survey_engine.domain.response.entity.SurveyResponse;
import com.roal.survey_engine.domain.response.repository.ResponseRepository;
import com.roal.survey_engine.domain.survey.dto.survey.CreateSurveyDto;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyDto;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyDtoMapper;
import com.roal.survey_engine.domain.survey.entity.*;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestion;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestionAnswer;
import com.roal.survey_engine.domain.survey.entity.question.OpenNumericQuestion;
import com.roal.survey_engine.domain.survey.entity.question.OpenTextQuestion;
import com.roal.survey_engine.domain.survey.repository.CampaignRepository;
import com.roal.survey_engine.domain.survey.repository.SurveyRepository;
import com.roal.survey_engine.domain.survey.repository.WorkspaceRepository;
import com.roal.survey_engine.domain.survey.service.SurveyService;
import com.roal.survey_engine.domain.user.UserAuthority;
import com.roal.survey_engine.domain.user.dto.UserRegistrationDto;
import com.roal.survey_engine.domain.user.entity.Role;
import com.roal.survey_engine.domain.user.repository.RoleRepository;
import com.roal.survey_engine.domain.user.repository.UserRepository;
import com.roal.survey_engine.domain.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

/*
    Add Survey for dev environment
 */
@Profile({"dev", "dev-h2", "devpg"})
@Component
class StartupCommandLineRunner implements CommandLineRunner {

    private final SurveyRepository surveyRepository;

    private final SurveyService surveyService;

    private final SurveyDtoMapper surveyDtoMapper;

    private final ResponseRepository responseRepository;
    private final CampaignRepository campaignRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;

    StartupCommandLineRunner(SurveyRepository surveyRepository,
                             SurveyService surveyService, SurveyDtoMapper surveyDtoMapper, ResponseRepository responseRepository,
                             CampaignRepository campaignRepository,
                             UserService userService,
                             RoleRepository roleRepository,
                             UserRepository userRepository,
                             WorkspaceRepository workspaceRepository) {
        this.surveyRepository = surveyRepository;
        this.surveyService = surveyService;
        this.surveyDtoMapper = surveyDtoMapper;
        this.responseRepository = responseRepository;
        this.campaignRepository = campaignRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        addRoles();
        addUsers();
        addSurvey();
    }

    private void addUsers() {
        if (userRepository.findAll().isEmpty()) {

            UserRegistrationDto user1 = new UserRegistrationDto("user1", "user1", "user1",
                "user1@example.de", Set.of("ROLE_USER"));
            UserRegistrationDto user2 = new UserRegistrationDto("user2", "user2", "user2",
                "user2@example.de", Set.of("ROLE_USER"));
            UserRegistrationDto admin = new UserRegistrationDto("admin", "admin", "admin",
                "admin@example.de", Set.of("ROLE_USER", "ROLE_ADMIN"));

            userService.create(user1);
            userService.create(user2);
            userService.create(admin);
        }
    }

    private void addSurvey() {

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("admin", ""
                , Set.of(new SimpleGrantedAuthority(UserAuthority.Constants.ADMIN_VALUE))));

        var workspace = new Workspace("default Workspace");

        workspaceRepository.save(workspace);
        var survey = new Survey()
                .setDescription("This is a Survey Description")
                .setTitle("This is a Survey Title")
                .setWorkspace(workspace)
                .addSurveyPage(new SurveyPage()
                        .addSurveyElement(new OpenTextQuestion("This is an open question?"))
                        .addSurveyElement(new ClosedQuestion("This is an closed question")
                                .addAnswer(new ClosedQuestionAnswer("First answer"))
                                .addAnswer(new ClosedQuestionAnswer("Second answer"))))
                .addSurveyPage(new SurveyPage()
                        .addSurveyElement(new OpenTextQuestion("This is another open question"))
                        .addSurveyElement(new OpenNumericQuestion("This is a numeric question"))
                        .addSurveyElement(new OpenNumericQuestion("This is a another numeric question")));
        var campaign = new Campaign()
                .setDateRange(new DateRange(LocalDateTime.now(), LocalDateTime.now().plus(1, ChronoUnit.YEARS)))
                .setActive(true)
                .setHidden(false);

        SurveyDto surveyDto = surveyDtoMapper.entityToDto(survey);
        var createSurveyDto = new CreateSurveyDto(surveyDto.title(),
                surveyDto.description(),
                surveyDto.workspaceId(),
                surveyDto.surveyPages());
        surveyDto = surveyService.create(createSurveyDto, surveyDto.workspaceId());
        Survey surveyById = surveyService.findSurveyById(surveyDto.id());
//        surveyRepository.save(survey);
        campaignRepository.save(campaign.setSurvey(surveyById));

        addResponse(surveyById, campaign);


    }

    private void addResponse(Survey survey, Campaign campaign) {
        ClosedQuestion closedQuestion = (ClosedQuestion) survey.getSurveyPages()
                .stream()
                .flatMap(surveyPage -> surveyPage.getSurveyPageElements().stream())
                .filter(element -> element instanceof ClosedQuestion)
                .findFirst().get();

        OpenTextQuestion openTextQuestion = (OpenTextQuestion) survey.getSurveyPages()
                .stream()
                .flatMap(surveyPage -> surveyPage.getSurveyPageElements().stream())
                .filter(element -> element instanceof OpenTextQuestion)
                .findFirst().get();

        OpenTextQuestion openTextQuestion2 = (OpenTextQuestion) survey.getSurveyPages()
                .stream()
                .flatMap(surveyPage -> surveyPage.getSurveyPageElements().stream())
                .filter(element -> element instanceof OpenTextQuestion)
                .skip(1)
                .findFirst().get();


        OpenNumericQuestion openNumericQuestion = (OpenNumericQuestion) survey.getSurveyPages()
                .stream()
                .flatMap(surveyPage -> surveyPage.getSurveyPageElements().stream())
                .filter(element -> element instanceof OpenNumericQuestion)
                .findFirst().get();

        OpenNumericQuestion openNumericQuestion2 = (OpenNumericQuestion) survey.getSurveyPages()
                .stream()
                .flatMap(surveyPage -> surveyPage.getSurveyPageElements().stream())
                .filter(element -> element instanceof OpenNumericQuestion)
                .skip(1)
                .findFirst().get();

        var response = new SurveyResponse()
                .setCampaign(campaign)
                .addElement(new ClosedQuestionResponse().setClosedQuestion(closedQuestion).setAnswers(Set.of(1)))
                .addElement(new OpenTextQuestionResponse().setOpenQuestion(openTextQuestion).setAnswer("answer"))
                .addElement(new OpenTextQuestionResponse().setOpenQuestion(openTextQuestion2).setAnswer("answer2"))
                .addElement(new OpenNumericQuestionResponse().setOpenNumericQuestion(openNumericQuestion).setAnswer(2))
                .addElement(new OpenNumericQuestionResponse().setOpenNumericQuestion(openNumericQuestion2).setAnswer(9));

        var response2 = new SurveyResponse()
                .setCampaign(campaign)
                .addElement(new ClosedQuestionResponse().setClosedQuestion(closedQuestion).setAnswers(Set.of(1)))
                .addElement(new OpenTextQuestionResponse().setOpenQuestion(openTextQuestion).setAnswer("answer"))
                .addElement(new OpenTextQuestionResponse().setOpenQuestion(openTextQuestion2).setAnswer("answer2"))
                .addElement(new OpenNumericQuestionResponse().setOpenNumericQuestion(openNumericQuestion).setAnswer(1))
                .addElement(new OpenNumericQuestionResponse().setOpenNumericQuestion(openNumericQuestion2).setAnswer(7));

        responseRepository.save(response);
        responseRepository.save(response2);
    }

    private void addRoles() {
        int size = roleRepository.findAll().size();
        if (size == 0) {
            List<Role> roles = List.of(
                    new Role("ROLE_USER"),
                    new Role("ROLE_ADMIN"));
            roleRepository.saveAll(roles);
        }
    }
}

