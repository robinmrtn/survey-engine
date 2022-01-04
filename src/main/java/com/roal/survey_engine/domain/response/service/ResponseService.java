package com.roal.survey_engine.domain.response.service;

import com.roal.survey_engine.domain.response.dto.SurveyResponseDto;
import com.roal.survey_engine.domain.response.dto.mapping.ResponseDtoMapper;
import com.roal.survey_engine.domain.response.entity.SurveyResponse;
import com.roal.survey_engine.domain.response.exception.ResponseNotFoundException;
import com.roal.survey_engine.domain.response.repository.ResponseRepository;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.service.CampaignService;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void insertSurveyResponseDto(long campaignId, SurveyResponseDto surveyResponseDto) {
        Campaign campaign = campaignService.findCampaignById(campaignId);
        SurveyResponse surveyResponse =
                responseDtoMapper.mapSurveyResponseDtoToSurveyResponse(campaign, surveyResponseDto);
        responseRepository.save(surveyResponse);
    }

    public List<SurveyResponse> getResponsesByCampaignId(long id) {
        return responseRepository.findAllByCampaignId(id);
    }

    public SurveyResponse getResponseById(long id) {
        return responseRepository.findById(id).orElseThrow(() -> new ResponseNotFoundException(id));
    }

}
