package com.roal.survey_engine.domain.survey.service;

import com.roal.survey_engine.domain.survey.dto.SurveyDto;
import com.roal.survey_engine.domain.survey.dto.SurveyDtoMapper;
import com.roal.survey_engine.domain.survey.dto.SurveyListElementDto;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.exception.SurveyNotFoundException;
import com.roal.survey_engine.domain.survey.repository.CampaignRepository;
import com.roal.survey_engine.domain.survey.repository.SurveyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;

    private final CampaignRepository campaignRepository;

    private final SurveyDtoMapper surveyDtoMapper;

    public SurveyService(SurveyRepository surveyRepository,
                         CampaignRepository campaignRepository,
                         SurveyDtoMapper surveyDtoMapper) {
        this.surveyRepository = surveyRepository;
        this.campaignRepository = campaignRepository;
        this.surveyDtoMapper = surveyDtoMapper;
    }

    public Survey save(Survey survey) {
        return surveyRepository.save(survey);
    }

    public SurveyDto saveDto(SurveyDto surveyDto) {
        Survey survey = surveyDtoMapper.dtoToEntity(surveyDto);
        surveyRepository.save(survey);
        return surveyDtoMapper.entityToDto(survey);
    }

    public Survey findSurveyById(long id) {
        return surveyRepository.findById(id).orElseThrow(() -> new SurveyNotFoundException(id));
    }

    public SurveyDto findSurveyByCampaignId(long campaignId) {
        var campaign = campaignRepository
                .findById(campaignId).orElseThrow(() -> new SurveyNotFoundException(campaignId));
        if (campaign.getSurvey() == null) {
            throw new SurveyNotFoundException(campaignId);
        }
        return surveyDtoMapper.entityToDto(campaign.getSurvey());
    }

    public Page<SurveyListElementDto> getPublicAndActiveSurveys(Pageable pageable) {
        return getSurveysFromCampaigns(campaignRepository.findByHiddenIsFalseAndActiveIsTrue(pageable));
    }

    private Page<SurveyListElementDto> getSurveysFromCampaigns(Page<Campaign> campaigns) {
        return campaigns.map(SurveyListElementDto::fromEntity);
    }
}