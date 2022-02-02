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
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/*
    Add Survey for dev environment
 */
@Profile({"dev", "dev-h2"})
@Component
class StartupCommandLineRunner implements CommandLineRunner {

    private final SurveyRepository surveyRepository;
    private final CampaignRepository campaignRepository;

    StartupCommandLineRunner(SurveyRepository surveyRepository, CampaignRepository campaignRepository) {
        this.surveyRepository = surveyRepository;
        this.campaignRepository = campaignRepository;
    }

    @Override
    public void run(String... args) throws Exception {
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

    }
}

