package com.roal.survey_engine.domain.survey.service;

import com.roal.survey_engine.domain.survey.dto.campaign.CampaignDto;
import com.roal.survey_engine.domain.survey.dto.campaign.CampaignDtoMapper;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.DateRange;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.exception.SurveyNotFoundException;
import com.roal.survey_engine.domain.survey.repository.CampaignRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final CampaignDtoMapper campaignDtoMapper;
    private final SurveyService surveyService;

    public CampaignService(CampaignRepository campaignRepository,
                           CampaignDtoMapper campaignDtoMapper,
                           SurveyService surveyService) {
        this.campaignRepository = campaignRepository;
        this.campaignDtoMapper = campaignDtoMapper;
        this.surveyService = surveyService;
    }

    public Campaign findCampaignById(long id) {
        return campaignRepository.findById(id).orElseThrow(SurveyNotFoundException::new);
    }

    public CampaignDto findCampaignDtoById(long id) {
        Campaign campaignById = findCampaignById(id);
        return campaignDtoMapper.entityToDto(campaignById);
    }

    public boolean existsById(long id) {
        return campaignRepository.existsById(id);
    }

    @Transactional
    public CampaignDto insertDto(CampaignDto campaignDto) {
        Campaign campaign = campaignDtoMapper.dtoToEntity(campaignDto);
        Campaign savedCampaign = campaignRepository.save(campaign);
        return campaignDtoMapper.entityToDto(savedCampaign);
    }

    @Transactional
    public CampaignDto addSurveyToCampaign(long surveyId, long campaignId) {
        Survey surveyById = surveyService.findSurveyById(surveyId);
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Campaign with id '" + campaignId + "' not found"));

        campaign.setSurvey(surveyById);
        campaignRepository.save(campaign);

        return campaignDtoMapper.entityToDto(campaign);
    }

    @Transactional
    public CampaignDto updateCampaign(CampaignDto campaignDto, long id) {
        return campaignRepository.findById(id)
                .map(campaign -> {
                    campaign.setTitle(campaignDto.title());
                    campaign.setHidden(campaignDto.hidden());
                    campaign.setActive(campaignDto.active());
                    campaign.setDateRange(new DateRange(campaignDto.from(), campaignDto.to()));
                    return campaignDtoMapper.entityToDto(campaignRepository.save(campaign));
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Campaign with id '" + id + "' not found"));
    }

    @Transactional
    public void deleteCampaign(long id) {
        campaignRepository.deleteById(id);
    }
}
