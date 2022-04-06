package com.roal.survey_engine.domain.response.service;

import com.roal.survey_engine.domain.response.dto.SurveyResponseDto;
import com.roal.survey_engine.domain.response.dto.mapping.ResponseDtoMapper;
import com.roal.survey_engine.domain.response.entity.SurveyResponse;
import com.roal.survey_engine.domain.response.exception.ResponseNotFoundException;
import com.roal.survey_engine.domain.response.repository.ResponseRepository;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.service.CampaignService;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class ResponseService {

    private final ResponseDtoMapper responseDtoMapper;

    private final ResponseRepository responseRepository;

    private final CampaignService campaignService;

    private final Hashids responseHashids;

    public ResponseService(ResponseDtoMapper responseDtoMapper,
                           ResponseRepository responseRepository,
                           CampaignService campaignService,
                           @Qualifier("responseHashids") Hashids responseHashids) {
        this.responseDtoMapper = responseDtoMapper;
        this.responseRepository = responseRepository;
        this.campaignService = campaignService;
        this.responseHashids = responseHashids;
    }

    @Transactional
    public SurveyResponseDto insertSurveyResponseDto(String campaignId, SurveyResponseDto surveyResponseDto) {
        Campaign campaign = campaignService.findCampaignById(campaignId);
        SurveyResponse surveyResponse =
                responseDtoMapper.dtoToEntity(campaign, surveyResponseDto);
        SurveyResponse savedSurveyResponse = responseRepository.save(surveyResponse);
        return responseDtoMapper.entityToDto(savedSurveyResponse);
    }

    public Page<SurveyResponseDto> getResponsesByCampaignId(String hashid, Pageable pageable) {
        long id = hashidToId(hashid);
        Page<SurveyResponseDto> responseDtos = responseRepository.findAllByCampaignId(id, pageable)
                .map(responseDtoMapper::entityToDto);

        if (responseDtos.getTotalElements() == 0 && !campaignService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign with id " + id + " not found");
        }

        return responseDtos;
    }

    public SurveyResponseDto getResponseById(String hashid) {
        long id = hashidToId(hashid);
        SurveyResponse surveyResponse = responseRepository.findById(id)
                .orElseThrow(() -> new ResponseNotFoundException(id));
        return responseDtoMapper.entityToDto(surveyResponse);
    }

    @Transactional
    public void deleteResponseById(String hashid) {
        long responseId = hashidToId(hashid);
        responseRepository.deleteById(responseId);
    }

    private long hashidToId(String hashid) {
        long[] decode = responseHashids.decode(hashid);
        if (decode.length == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return decode[0];
    }
}
