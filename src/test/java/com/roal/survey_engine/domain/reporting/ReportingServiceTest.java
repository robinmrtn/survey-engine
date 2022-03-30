package com.roal.survey_engine.domain.reporting;

import com.roal.survey_engine.domain.reporting.dto.out.AbstractElementReportingDto;
import com.roal.survey_engine.domain.reporting.dto.out.NumericReportingDto;
import com.roal.survey_engine.domain.reporting.service.ReportingService;
import com.roal.survey_engine.domain.response.entity.ClosedQuestionResponse;
import com.roal.survey_engine.domain.response.entity.OpenNumericQuestionResponse;
import com.roal.survey_engine.domain.response.entity.SurveyResponse;
import com.roal.survey_engine.domain.response.repository.ResponseRepository;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.SurveyPage;
import com.roal.survey_engine.domain.survey.entity.question.AbstractSurveyElement;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestion;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestionAnswer;
import com.roal.survey_engine.domain.survey.entity.question.OpenNumericQuestion;
import com.roal.survey_engine.domain.survey.repository.CampaignRepository;
import com.roal.survey_engine.domain.survey.repository.SurveyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@SpringBootTest
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
    void testNumericReporting_Success() {

        var survey = new Survey()
            .addSurveyPage(new SurveyPage()
                .addSurveyElement(new OpenNumericQuestion("question")));
        var campaign = new Campaign().setSurvey(survey);
        Survey savedSurvey = surveyRepository.saveAndFlush(survey);
        Campaign savedCampaign = campaignRepository.saveAndFlush(campaign);

        AbstractSurveyElement element = savedSurvey.getSurveyPages().get(0).getSurveyPageElements().get(0);

        var surveyResponse = new SurveyResponse()
            .setSurvey(survey)
            .addElement(new OpenNumericQuestionResponse()
                .setOpenNumericQuestion((OpenNumericQuestion) element).setAnswer(1.0));

        var surveyResponse2 = new SurveyResponse()
            .setSurvey(survey)
            .setCampaign(campaign)
            .addElement(new OpenNumericQuestionResponse()
                .setOpenNumericQuestion((OpenNumericQuestion) element).setAnswer(2.0));
        responseRepository.saveAllAndFlush(List.of(surveyResponse, surveyResponse2));
        NumericReportingDto numericReportingDtoByElementId =
            reportingService.getNumericReportingDto(element.getId(), savedCampaign.getId());

        Assertions.assertEquals(element.getId(), numericReportingDtoByElementId.elementId());
        Assertions.assertEquals(1.5, numericReportingDtoByElementId.avg());
        Assertions.assertEquals(1.0, numericReportingDtoByElementId.min());
        Assertions.assertEquals(2.0, numericReportingDtoByElementId.max());
        Assertions.assertEquals(2, numericReportingDtoByElementId.count());
    }

    @Test
    void testNumericReporting_NotFound() {

        var survey = new Survey()
            .addSurveyPage(new SurveyPage()
                .addSurveyElement(new OpenNumericQuestion("question")));
        var campaign = new Campaign().setSurvey(survey);
        Survey savedSurvey = surveyRepository.saveAndFlush(survey);
        Campaign savedCampaign = campaignRepository.saveAndFlush(campaign);

        AbstractSurveyElement element = savedSurvey.getSurveyPages().get(0).getSurveyPageElements().get(0);

        var surveyResponse = new SurveyResponse()
            .setSurvey(survey)
            .addElement(new OpenNumericQuestionResponse()
                .setOpenNumericQuestion((OpenNumericQuestion) element).setAnswer(1.0));

        var surveyResponse2 = new SurveyResponse()
            .setSurvey(survey)
            .setCampaign(campaign)
            .addElement(new OpenNumericQuestionResponse()
                .setOpenNumericQuestion((OpenNumericQuestion) element).setAnswer(2.0));
        responseRepository.saveAllAndFlush(List.of(surveyResponse, surveyResponse2));


        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class, () -> {
            NumericReportingDto numericReportingDtoByElementId =
                reportingService.getNumericReportingDto(element.getId(), savedCampaign.getId() + 1);
        });

        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseStatusException.getStatus());
    }

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

        List<AbstractElementReportingDto> reportsByCampaignId =
            reportingService.getReportsByCampaignId(savedCampaign.getId());

        Assertions.assertEquals(3, reportsByCampaignId.size());
        Assertions.assertEquals(5, reportsByCampaignId.get(0).count());
        Assertions.assertEquals("cat", reportsByCampaignId.get(0).type());
        Assertions.assertEquals("cat", reportsByCampaignId.get(1).type());
        Assertions.assertEquals(3, reportsByCampaignId.get(1).count());
        Assertions.assertEquals(3, reportsByCampaignId.get(2).count());
        Assertions.assertEquals("num", reportsByCampaignId.get(2).type());
        Assertions.assertEquals(2.0, ((NumericReportingDto) reportsByCampaignId.get(2)).median());
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

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            List<AbstractElementReportingDto> reportsByCampaignId =
                reportingService.getReportsByCampaignId(savedCampaign.getId() + 1);
        });
    }
}
