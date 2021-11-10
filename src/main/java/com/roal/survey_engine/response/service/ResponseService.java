package com.roal.survey_engine.response.service;

import com.roal.survey_engine.response.dto.SurveyResponseDto;
import com.roal.survey_engine.response.dto.mapping.ResponseDtoMapper;
import com.roal.survey_engine.response.entity.SurveyResponse;
import com.roal.survey_engine.response.repository.ResponseRepository;
import com.roal.survey_engine.survey.entity.Campaign;
import com.roal.survey_engine.survey.exception.SurveyNotFoundException;
import com.roal.survey_engine.survey.service.CampaignService;
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
        return responseRepository.findById(id).orElseThrow(SurveyNotFoundException::new);
    }

}
