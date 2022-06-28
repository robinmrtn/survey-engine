package com.roal.survey_engine.domain.survey.service;

import com.roal.survey_engine.domain.survey.dto.survey.SurveyDto;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class SurveyQueryProducer {

    private final JmsTemplate jmsTemplate;

    public SurveyQueryProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendAddSurveyMessage(SurveyDto surveyDto) {
        jmsTemplate.convertAndSend("surveyQueryConsumer", surveyDto);
        System.out.println("Message send");
    }
}
