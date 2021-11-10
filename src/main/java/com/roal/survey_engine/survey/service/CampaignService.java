package com.roal.survey_engine.survey.service;

import com.roal.survey_engine.survey.entity.Campaign;
import com.roal.survey_engine.survey.exception.SurveyNotFoundException;
import com.roal.survey_engine.survey.repository.CampaignRepository;
import org.springframework.stereotype.Service;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;

    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public Campaign findCampaignById(long id) {
        return campaignRepository.findById(id).orElseThrow(SurveyNotFoundException::new);
    }
}
