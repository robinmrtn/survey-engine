package com.roal.survey_engine.domain.survey.service;

import com.roal.survey_engine.domain.survey.dto.campaign.CampaignDto;
import com.roal.survey_engine.domain.survey.dto.campaign.CampaignDtoMapper;
import com.roal.survey_engine.domain.survey.dto.campaign.CreateCampaignDto;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.DateRange;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.exception.CampaignNotFoundException;
import com.roal.survey_engine.domain.survey.repository.CampaignRepository;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final CampaignDtoMapper campaignDtoMapper;
    private final SurveyService surveyService;
    private final Hashids campaignHashids;

    public CampaignService(CampaignRepository campaignRepository,
                           CampaignDtoMapper campaignDtoMapper,
                           SurveyService surveyService,
                           @Qualifier("campaignHashids") Hashids campaignHashids) {
        this.campaignRepository = campaignRepository;
        this.campaignDtoMapper = campaignDtoMapper;
        this.surveyService = surveyService;
        this.campaignHashids = campaignHashids;
    }

    @Transactional(readOnly = true)
    public Campaign findCampaignById(long id) {
        return campaignRepository.findById(id).orElseThrow(CampaignNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Campaign findCampaignById(String hashid) {
        long id = hashidToId(hashid);
        return findCampaignById(id);
    }

    @Transactional(readOnly = true)
    public CampaignDto findCampaignDtoById(String hashid) {
        long id = hashidToId(hashid);
        Campaign campaignById = findCampaignById(id);
        return campaignDtoMapper.entityToDto(campaignById);
    }

    @Transactional(readOnly = true)
    public boolean existsById(long id) {
        return campaignRepository.existsById(id);
    }

    @Transactional
    public CampaignDto create(CreateCampaignDto campaignDto, String surveyId) {
        Survey surveyById = surveyService.findSurveyById(surveyId);
        Campaign campaign = campaignDtoMapper.dtoToEntity(campaignDto);
        campaign.setSurvey(surveyById);

        Campaign savedCampaign = campaignRepository.save(campaign);
        return campaignDtoMapper.entityToDto(savedCampaign);
    }

    @Transactional
    public CampaignDto update(CampaignDto campaignDto, String hashid) {
        long id = hashidToId(hashid);
        return campaignRepository.findById(id)
            .map(campaign -> {
                campaign.setTitle(campaignDto.title());
                campaign.setHidden(campaignDto.hidden());
                campaign.setActive(campaignDto.active());
                campaign.setDateRange(new DateRange(campaignDto.from(), campaignDto.to()));
                return campaignDtoMapper.entityToDto(campaignRepository.save(campaign));
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Campaign with id '" + id + "' not found"));
    }

    @Transactional
    public void deleteCampaignById(String hashid) {
        long id = hashidToId(hashid);
        campaignRepository.deleteById(id);
    }

    private long hashidToId(String hashid) {
        long[] decode = campaignHashids.decode(hashid);
        if (decode.length == 0) {
            throw new CampaignNotFoundException(hashid);
        }
        return decode[0];
    }
}
