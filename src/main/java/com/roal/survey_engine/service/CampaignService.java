package com.roal.survey_engine.service;

import com.roal.survey_engine.entity.survey.Campaign;
import com.roal.survey_engine.exception.SurveyNotFoundException;
import com.roal.survey_engine.repository.CampaignRepository;
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
