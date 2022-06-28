package com.roal.survey_engine.domain.survey.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyDto;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyDtoMapper;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyQueryDto;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.exception.CampaignNotFoundException;
import com.roal.survey_engine.domain.survey.exception.SurveyNotFoundException;
import com.roal.survey_engine.domain.survey.repository.SurveyQueryRepository;
import org.springframework.stereotype.Service;

@Service
public class SurveyQueryService {

    private final ObjectMapper objectmapper = new ObjectMapper();

    private final SurveyQueryRepository surveyQueryRepository;

    private final SurveyDtoMapper surveyDtoMapper;


    public SurveyQueryService(SurveyQueryRepository surveyQueryRepository,
                              SurveyDtoMapper surveyDtoMapper) {
        this.surveyQueryRepository = surveyQueryRepository;
        this.surveyDtoMapper = surveyDtoMapper;
    }

    public SurveyQueryDto findSurveyById(String hashid) {
        String surveyById = surveyQueryRepository.findSurveyById(hashid);

        try {
            return fromString(surveyById);
        } catch (JsonProcessingException e) {
            throw new SurveyNotFoundException(hashid);
        }

    }

    public void addCampaign(Campaign campaign, String hashid) {
        SurveyDto surveyDto = surveyDtoMapper.entityToDto(campaign.getSurvey());
        SurveyQueryDto queryDto = SurveyQueryDto.from(surveyDto);
        surveyQueryRepository.addSurvey(queryDto, hashid);
    }

    public SurveyQueryDto findSurveyByCampaignId(String hashid) {

        String surveyByCampaignId = surveyQueryRepository.findSurveyByCampaignId(hashid);

        try {
            return fromString(surveyByCampaignId);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            throw new CampaignNotFoundException(hashid);
        }
    }

    public void addSurvey(SurveyQueryDto queryDto) {
        surveyQueryRepository.addSurvey(queryDto);
    }

    public void addSurvey(SurveyDto surveyDto) {
        SurveyQueryDto surveyQueryDto = SurveyQueryDto.from(surveyDto);
        addSurvey(surveyQueryDto);
    }

    private SurveyQueryDto fromString(String s) throws JsonProcessingException {
        return objectmapper.readValue(s, SurveyQueryDto.class);
    }
}
