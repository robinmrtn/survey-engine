package com.roal.survey_engine.survey.service;

import com.roal.survey_engine.survey.dto.SurveyListElementDto;
import com.roal.survey_engine.survey.entity.Campaign;
import com.roal.survey_engine.survey.entity.Survey;
import com.roal.survey_engine.survey.exception.SurveyNotFoundException;
import com.roal.survey_engine.survey.repository.CampaignRepository;
import com.roal.survey_engine.survey.repository.SurveyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;

    private final CampaignRepository campaignRepository;

    public SurveyService(SurveyRepository surveyRepository, CampaignRepository campaignRepository) {
        this.surveyRepository = surveyRepository;
        this.campaignRepository = campaignRepository;
    }

    public Survey save(Survey survey) {
        return surveyRepository.save(survey);
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

    public List<SurveyListElementDto> getPublicAndActiveSurveys() {
        return getSurveysFromCampaigns(campaignRepository.findByHiddenIsFalseAndActiveIsTrue());
    }

    private List<SurveyListElementDto> getSurveysFromCampaigns(List<Campaign> campaigns) {

        return campaigns.stream()
                .map((campaign) -> new SurveyListElementDto(campaign.getId(),
                        campaign.getSurvey().getTitle(),
                        campaign.getSurvey().getDescription()))
                .collect(Collectors.toList());
    }
}