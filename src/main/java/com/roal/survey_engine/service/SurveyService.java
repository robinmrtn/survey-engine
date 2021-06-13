package com.roal.survey_engine.service;

import com.roal.survey_engine.entity.survey.Survey;
import com.roal.survey_engine.exception.SurveyNotFoundException;
import com.roal.survey_engine.repository.CampaignRepository;
import com.roal.survey_engine.repository.SurveyRepository;
import org.springframework.stereotype.Service;

@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;

    private final CampaignRepository campaignRepository;

    public SurveyService(SurveyRepository surveyRepository, CampaignRepository campaignRepository) {
        this.surveyRepository = surveyRepository;
        this.campaignRepository = campaignRepository;
    }

    public Survey findSurveyById(long id) {
        return surveyRepository.findById(id).orElseThrow(SurveyNotFoundException::new);
    }

    public Survey findSurveyByCampaignId(long campaignId) {
        var campaign = campaignRepository
                .findById(campaignId).orElseThrow(SurveyNotFoundException::new);
        if (campaign.getSurvey() == null) {
            throw new SurveyNotFoundException();
        }
        return campaign.getSurvey();
    }
}
