package com.roal.survey_engine.service;

import com.roal.survey_engine.dto.response.SurveyResponseDto;
import com.roal.survey_engine.dto.response.mapping.ResponseDtoMapper;
import com.roal.survey_engine.entity.response.SurveyResponse;
import com.roal.survey_engine.entity.survey.Campaign;
import com.roal.survey_engine.repository.ResponseRepository;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {

    private final ResponseDtoMapper responseDtoMapper;

    private final ResponseRepository responseRepository;

    public ResponseService(ResponseDtoMapper responseDtoMapper, ResponseRepository responseRepository) {
        this.responseDtoMapper = responseDtoMapper;
        this.responseRepository = responseRepository;
    }

    public void insertSurveyResponseDto(Campaign campaign, SurveyResponseDto surveyResponseDto) {
        SurveyResponse surveyResponse =
                responseDtoMapper.mapSurveyResponseDtoToSurveyResponse(campaign, surveyResponseDto);
        responseRepository.save(surveyResponse);

    }

}
