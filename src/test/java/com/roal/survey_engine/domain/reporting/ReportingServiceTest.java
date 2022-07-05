package com.roal.survey_engine.domain.reporting;

import com.roal.survey_engine.domain.reporting.dto.out.NumericReportingDto;
import com.roal.survey_engine.domain.reporting.dto.out.ReportingDto;
import com.roal.survey_engine.domain.reporting.service.ReportingService;
import com.roal.survey_engine.domain.response.entity.ClosedQuestionResponse;
import com.roal.survey_engine.domain.response.entity.OpenNumericQuestionResponse;
import com.roal.survey_engine.domain.response.entity.SurveyResponse;
import com.roal.survey_engine.domain.response.repository.ResponseRepository;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.SurveyPage;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestion;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestionAnswer;
import com.roal.survey_engine.domain.survey.entity.question.OpenNumericQuestion;
import com.roal.survey_engine.domain.survey.exception.CampaignNotFoundException;
import com.roal.survey_engine.domain.survey.repository.CampaignRepository;
import com.roal.survey_engine.domain.survey.repository.SurveyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles({"test"})
public class ReportingServiceTest {

    @Autowired
    ReportingService reportingService;

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    ResponseRepository responseRepository;

    @Autowired
    CampaignRepository campaignRepository;

    @Test
    void testCategoricalAndNumericReporting_Success() {
        var survey = new Survey()
            .addSurveyPage(new SurveyPage()
                .addSurveyElement(new OpenNumericQuestion("question"))
                .addSurveyElement(new ClosedQuestion()
                    .addAnswer(new ClosedQuestionAnswer("answer 1"))
                    .addAnswer(new ClosedQuestionAnswer("answer 2")))
                .addSurveyElement(new ClosedQuestion()
                    .addAnswer(new ClosedQuestionAnswer("answer 4"))
                    .addAnswer(new ClosedQuestionAnswer("answer 5"))));
        var campaign = new Campaign().setSurvey(survey);
        Survey savedSurvey = surveyRepository.save(survey);
        Campaign savedCampaign = campaignRepository.save(campaign);

        OpenNumericQuestion numericElement = (OpenNumericQuestion) savedSurvey
            .getSurveyPages().get(0).getSurveyPageElements().get(0);

        ClosedQuestion catElement = (ClosedQuestion) savedSurvey
            .getSurveyPages().get(0).getSurveyPageElements().get(1);

        ClosedQuestion catElement2 = (ClosedQuestion) savedSurvey
            .getSurveyPages().get(0).getSurveyPageElements().get(2);

        var surveyResponse = new SurveyResponse()
            .setSurvey(survey)
            .addElement(new OpenNumericQuestionResponse()
                .setOpenNumericQuestion(numericElement).setAnswer(1.0))
            .addElement(new ClosedQuestionResponse()
                .setClosedQuestion(catElement)
                .setAnswers(List.of(catElement.getAnswers().get(0))))
            .addElement(new ClosedQuestionResponse()
                .setClosedQuestion(catElement2)
                .setAnswers(List.of(catElement2.getAnswers().get(0))));

        var surveyResponse2 = new SurveyResponse()
            .setSurvey(survey)
            .setCampaign(campaign)
            .addElement(new OpenNumericQuestionResponse()
                .setOpenNumericQuestion((OpenNumericQuestion) numericElement).setAnswer(2.0))
            .addElement(new ClosedQuestionResponse()
                .setClosedQuestion(catElement)
                .setAnswers(catElement.getAnswers()))
            .addElement(new ClosedQuestionResponse()
                .setClosedQuestion(catElement2)
                .setAnswers(List.of(catElement2.getAnswers().get(1))));
        ;

        var surveyResponse3 = new SurveyResponse()
            .setSurvey(survey)
            .setCampaign(campaign)
            .addElement(new OpenNumericQuestionResponse()
                .setOpenNumericQuestion((OpenNumericQuestion) numericElement).setAnswer(2.0))
            .addElement(new ClosedQuestionResponse()
                .setClosedQuestion(catElement)
                .setAnswers(List.copyOf(catElement.getAnswers())))
            .addElement(new ClosedQuestionResponse()
                .setClosedQuestion(catElement2)
                .setAnswers(List.of(catElement2.getAnswers().get(1))));

        responseRepository.saveAll(List.of(surveyResponse, surveyResponse2, surveyResponse3));

        List<ReportingDto> reportsByCampaignId =
            reportingService.getReportsByCampaignId(savedCampaign.getId());

        Assertions.assertEquals(3, reportsByCampaignId.size());
        Assertions.assertEquals(5, reportsByCampaignId.get(0).count());
        Assertions.assertEquals("cat", reportsByCampaignId.get(0).type());
        Assertions.assertEquals("cat", reportsByCampaignId.get(1).type());
        Assertions.assertEquals(3, reportsByCampaignId.get(1).count());
        Assertions.assertEquals(3, reportsByCampaignId.get(2).count());
        Assertions.assertEquals("num", reportsByCampaignId.get(2).type());
        Assertions.assertEquals(2.0, ((NumericReportingDto) reportsByCampaignId.get(2)).percentiles().percentile50());
    }

    @Test
    void testCategoricalAndNumericReporting_NotFound() {
        var survey = new Survey()
            .addSurveyPage(new SurveyPage()
                .addSurveyElement(new OpenNumericQuestion("question"))
                .addSurveyElement(new ClosedQuestion()
                    .addAnswer(new ClosedQuestionAnswer("answer 1"))
                    .addAnswer(new ClosedQuestionAnswer("answer 2")))
                .addSurveyElement(new ClosedQuestion()
                    .addAnswer(new ClosedQuestionAnswer("answer 4"))
                    .addAnswer(new ClosedQuestionAnswer("answer 5"))));
        var campaign = new Campaign().setSurvey(survey);
        Survey savedSurvey = surveyRepository.save(survey);
        Campaign savedCampaign = campaignRepository.save(campaign);

        OpenNumericQuestion numericElement = (OpenNumericQuestion) savedSurvey
            .getSurveyPages().get(0).getSurveyPageElements().get(0);

        ClosedQuestion catElement = (ClosedQuestion) savedSurvey
            .getSurveyPages().get(0).getSurveyPageElements().get(1);

        ClosedQuestion catElement2 = (ClosedQuestion) savedSurvey
            .getSurveyPages().get(0).getSurveyPageElements().get(2);

        var surveyResponse = new SurveyResponse()
            .setSurvey(survey)
            .addElement(new OpenNumericQuestionResponse()
                .setOpenNumericQuestion(numericElement).setAnswer(1.0))
            .addElement(new ClosedQuestionResponse()
                .setClosedQuestion(catElement)
                .setAnswers(List.of(catElement.getAnswers().get(0))))
            .addElement(new ClosedQuestionResponse()
                .setClosedQuestion(catElement2)
                .setAnswers(List.of(catElement2.getAnswers().get(0))));

        var surveyResponse2 = new SurveyResponse()
            .setSurvey(survey)
            .setCampaign(campaign)
            .addElement(new OpenNumericQuestionResponse()
                .setOpenNumericQuestion((OpenNumericQuestion) numericElement).setAnswer(2.0))
            .addElement(new ClosedQuestionResponse()
                .setClosedQuestion(catElement)
                .setAnswers(catElement.getAnswers()))
            .addElement(new ClosedQuestionResponse()
                .setClosedQuestion(catElement2)
                .setAnswers(List.of(catElement2.getAnswers().get(1))));
        ;

        var surveyResponse3 = new SurveyResponse()
            .setSurvey(survey)
            .setCampaign(campaign)
            .addElement(new OpenNumericQuestionResponse()
                .setOpenNumericQuestion((OpenNumericQuestion) numericElement).setAnswer(2.0))
            .addElement(new ClosedQuestionResponse()
                .setClosedQuestion(catElement)
                .setAnswers(List.copyOf(catElement.getAnswers())))
            .addElement(new ClosedQuestionResponse()
                .setClosedQuestion(catElement2)
                .setAnswers(List.of(catElement2.getAnswers().get(1))));

        responseRepository.saveAll(List.of(surveyResponse, surveyResponse2, surveyResponse3));

        Assertions.assertThrows(CampaignNotFoundException.class, () -> {
            List<ReportingDto> reportsByCampaignId =
                    reportingService.getReportsByCampaignId(savedCampaign.getId() + 1);
        });
    }
}
