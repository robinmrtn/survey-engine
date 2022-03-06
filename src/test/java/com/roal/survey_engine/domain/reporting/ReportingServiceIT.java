package com.roal.survey_engine.domain.reporting;

import com.roal.survey_engine.domain.reporting.dto.out.NumericReportingDto;
import com.roal.survey_engine.domain.reporting.service.ReportingService;
import com.roal.survey_engine.domain.response.entity.OpenNumericQuestionResponse;
import com.roal.survey_engine.domain.response.entity.SurveyResponse;
import com.roal.survey_engine.domain.response.repository.ResponseRepository;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.SurveyPage;
import com.roal.survey_engine.domain.survey.entity.question.AbstractSurveyElement;
import com.roal.survey_engine.domain.survey.entity.question.OpenNumericQuestion;
import com.roal.survey_engine.domain.survey.repository.SurveyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ReportingServiceIT {

    @Autowired
    ReportingService reportingService;

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    ResponseRepository responseRepository;

    @Test
    void testNumericReporting() {
        var survey = new Survey()
                .addSurveyPage(new SurveyPage()
                        .addSurveyElement(new OpenNumericQuestion("question")));
        Survey savedSurvey = surveyRepository.saveAndFlush(survey);

        AbstractSurveyElement element = savedSurvey.getSurveyPages().get(0).getSurveyPageElements().get(0);

        var surveyResponse = new SurveyResponse()
                .setSurvey(survey)
                .addElement(new OpenNumericQuestionResponse()
                        .setOpenNumericQuestion((OpenNumericQuestion) element).setAnswer(1.0));

        var surveyResponse2 = new SurveyResponse()
                .setSurvey(survey)
                .addElement(new OpenNumericQuestionResponse()
                        .setOpenNumericQuestion((OpenNumericQuestion) element).setAnswer(2.0));
        responseRepository.saveAllAndFlush(List.of(surveyResponse, surveyResponse2));
        NumericReportingDto numericReportingDtoByElementId =
                reportingService.getNumericReportingDtoByElementId(element.getId());

        Assertions.assertEquals(element.getId(), numericReportingDtoByElementId.elementId());
        Assertions.assertEquals(1.5, numericReportingDtoByElementId.avg());
        Assertions.assertEquals(1.0, numericReportingDtoByElementId.min());
        Assertions.assertEquals(2.0, numericReportingDtoByElementId.max());
        Assertions.assertEquals(2, numericReportingDtoByElementId.count());

    }
}
