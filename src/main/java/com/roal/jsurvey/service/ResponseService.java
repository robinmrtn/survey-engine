package com.roal.jsurvey.service;

import com.roal.jsurvey.dto.ResponseDtoMapper;
import com.roal.jsurvey.dto.SurveyResponseDto;
import com.roal.jsurvey.entity.responses.SurveyResponse;
import com.roal.jsurvey.entity.survey.Campaign;
import com.roal.jsurvey.repository.ResponseRepository;
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
