package com.roal.jsurvey.service;

import com.roal.jsurvey.entity.survey.Campaign;
import com.roal.jsurvey.exception.SurveyNotFoundException;
import com.roal.jsurvey.repository.CampaignRepository;
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
