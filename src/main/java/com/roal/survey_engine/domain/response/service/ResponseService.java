package com.roal.survey_engine.domain.response.service;

import com.roal.survey_engine.domain.response.dto.SurveyResponseDto;
import com.roal.survey_engine.domain.response.dto.mapping.ResponseDtoMapper;
import com.roal.survey_engine.domain.response.entity.SurveyResponse;
import com.roal.survey_engine.domain.response.exception.ResponseNotFoundException;
import com.roal.survey_engine.domain.response.repository.ResponseRepository;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.service.CampaignService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ResponseService {

    private final ResponseDtoMapper responseDtoMapper;

    private final ResponseRepository responseRepository;

    private final CampaignService campaignService;

    public ResponseService(ResponseDtoMapper responseDtoMapper,
                           ResponseRepository responseRepository,
                           CampaignService campaignService) {
        this.responseDtoMapper = responseDtoMapper;
        this.responseRepository = responseRepository;
        this.campaignService = campaignService;
    }

    public SurveyResponseDto insertSurveyResponseDto(long campaignId, SurveyResponseDto surveyResponseDto) {
        Campaign campaign = campaignService.findCampaignById(campaignId);
        SurveyResponse surveyResponse =
                responseDtoMapper.dtoToEntity(campaign, surveyResponseDto);
        SurveyResponse savedSurveyResponse = responseRepository.save(surveyResponse);
        return responseDtoMapper.entityToDto(savedSurveyResponse);
    }

    public Page<SurveyResponseDto> getResponsesByCampaignId(long id, Pageable pageable) {

        Page<SurveyResponseDto> responseDtos = responseRepository.findAllByCampaignId(id, pageable)
                .map(responseDtoMapper::entityToDto);

        if (responseDtos.getTotalElements() == 0 && !campaignService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign with id " + id + " not found");
        }

        return responseDtos;
    }

    public SurveyResponseDto getResponseById(long id) {
        SurveyResponse surveyResponse = responseRepository.findById(id)
                .orElseThrow(() -> new ResponseNotFoundException(id));
        return responseDtoMapper.entityToDto(surveyResponse);
    }

}
