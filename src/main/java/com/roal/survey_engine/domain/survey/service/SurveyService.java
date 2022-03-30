package com.roal.survey_engine.domain.survey.service;

import com.roal.survey_engine.domain.survey.dto.survey.SurveyDto;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyDtoMapper;
import com.roal.survey_engine.domain.survey.dto.survey.out.SurveyListElementDto;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.exception.SurveyNotFoundException;
import com.roal.survey_engine.domain.survey.repository.CampaignRepository;
import com.roal.survey_engine.domain.survey.repository.SurveyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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

    @Transactional
    public Survey save(Survey survey) {
        return surveyRepository.save(survey);
    }

    @Transactional
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

    public Page<SurveyListElementDto> getPublicSurveys(Pageable pageable) {
        return getSurveysFromCampaigns(campaignRepository.findPublicCampaigns(pageable));
    }

    private Page<SurveyListElementDto> getSurveysFromCampaigns(Page<Campaign> campaigns) {
        return campaigns.map(SurveyListElementDto::fromEntity);
    }

    @Transactional
    public void deleteSurveyById(Long id) {
        campaignRepository.deleteById(id);
    }
}
