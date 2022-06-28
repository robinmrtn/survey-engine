package com.roal.survey_engine.domain.survey.service;

import com.roal.survey_engine.domain.survey.dto.survey.SurveyDto;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class SurveyQueryConsumer {

    private final SurveyQueryService surveyQueryService;

    public SurveyQueryConsumer(SurveyQueryService surveyQueryService) {
        this.surveyQueryService = surveyQueryService;
    }

    @JmsListener(destination = "surveyQueryConsumer", containerFactory = "listenerFactory")
    public void consumeAddSurveyMessage(SurveyDto surveyDto) {
        surveyQueryService.addSurvey(surveyDto);
        System.out.println("Message consumed");
    }

}
