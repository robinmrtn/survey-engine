package com.roal.survey_engine.runner;

import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.SurveyPage;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestion;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestionAnswer;
import com.roal.survey_engine.domain.survey.entity.question.OpenNumericQuestion;
import com.roal.survey_engine.domain.survey.entity.question.OpenTextQuestion;
import com.roal.survey_engine.domain.survey.repository.CampaignRepository;
import com.roal.survey_engine.domain.survey.repository.SurveyRepository;
import com.roal.survey_engine.domain.user.UserDto;
import com.roal.survey_engine.domain.user.entity.Role;
import com.roal.survey_engine.domain.user.repository.RoleRepository;
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

    StartupCommandLineRunner(SurveyRepository surveyRepository,
                             CampaignRepository campaignRepository,
                             UserService userService,
                             RoleRepository roleRepository) {
        this.surveyRepository = surveyRepository;
        this.campaignRepository = campaignRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        addRoles();
        var openQuestion = new OpenTextQuestion("This is an open question?")
                .setPosition(1);

        var surveyPage = new SurveyPage()
                .addSurveyElement(openQuestion);

        var survey = new Survey()
                .setDescription("This is a Survey Description")
                .setTitle("This is a Survey Title")
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
                .setId(1)
                .setActive(true)
                .setHidden(false);

        surveyRepository.save(survey);
        Campaign savedCampaign = campaignRepository.save(campaign);
        System.out.println(savedCampaign.getId());

        UserDto peter = new UserDto(null, "peter", "peter", Set.of("USER"));
        UserDto max = new UserDto(null, "max", "max", Set.of("USER"));
        UserDto admin = new UserDto(null, "admin", "admin", Set.of("USER", "ADMIN"));

        userService.save(peter);
        userService.save(max);
        userService.save(admin);
    }

    private void addRoles() {

        List<Role> roles = List.of(
                new Role("USER"),
                new Role("ADMIN"));
        roleRepository.saveAll(roles);

    }
}

