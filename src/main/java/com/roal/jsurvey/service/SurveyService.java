package com.roal.jsurvey.service;

import com.roal.jsurvey.entity.survey.Survey;
import com.roal.jsurvey.exception.SurveyNotFoundException;
import com.roal.jsurvey.repository.CampaignRepository;
import com.roal.jsurvey.repository.SurveyRepository;
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
