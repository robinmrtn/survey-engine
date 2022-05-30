package com.roal.survey_engine.runner;

import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.SurveyPage;
import com.roal.survey_engine.domain.survey.entity.Workspace;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestion;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestionAnswer;
import com.roal.survey_engine.domain.survey.entity.question.OpenNumericQuestion;
import com.roal.survey_engine.domain.survey.entity.question.OpenTextQuestion;
import com.roal.survey_engine.domain.survey.repository.CampaignRepository;
import com.roal.survey_engine.domain.survey.repository.SurveyRepository;
import com.roal.survey_engine.domain.survey.repository.WorkspaceRepository;
import com.roal.survey_engine.domain.user.dto.UserRegistrationDto;
import com.roal.survey_engine.domain.user.entity.Role;
import com.roal.survey_engine.domain.user.repository.RoleRepository;
import com.roal.survey_engine.domain.user.repository.UserRepository;
import com.roal.survey_engine.domain.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/*
    Add Survey for dev environment
 */
@Profile({"dev", "dev-h2", "devpg"})
@Component
class StartupCommandLineRunner implements CommandLineRunner {

    private final SurveyRepository surveyRepository;
    private final CampaignRepository campaignRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;

    StartupCommandLineRunner(SurveyRepository surveyRepository,
                             CampaignRepository campaignRepository,
                             UserService userService,
                             RoleRepository roleRepository,
                             UserRepository userRepository,
                             WorkspaceRepository workspaceRepository) {
        this.surveyRepository = surveyRepository;
        this.campaignRepository = campaignRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.workspaceRepository = workspaceRepository;
    }

    @Override
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
                .addSurveyElement(new OpenNumericQuestion("This is a numeric question")));
        var campaign = new Campaign()
            .setSurvey(survey)
            .setActive(true)
            .setHidden(false);

        surveyRepository.save(survey);
        campaignRepository.save(campaign);
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

