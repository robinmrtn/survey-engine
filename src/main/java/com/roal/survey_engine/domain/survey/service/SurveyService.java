package com.roal.survey_engine.domain.survey.service;

import com.roal.survey_engine.domain.survey.dto.survey.SurveyDto;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyDtoMapper;
import com.roal.survey_engine.domain.survey.dto.survey.out.SurveyListElementDto;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.Workspace;
import com.roal.survey_engine.domain.survey.exception.CampaignNotFoundException;
import com.roal.survey_engine.domain.survey.exception.SurveyNotFoundException;
import com.roal.survey_engine.domain.survey.repository.CampaignRepository;
import com.roal.survey_engine.domain.survey.repository.SurveyRepository;
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
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final CampaignRepository campaignRepository;
    private final SurveyDtoMapper surveyDtoMapper;
    private final Hashids surveyHashids;
    private final WorkspaceService workspaceService;

    public SurveyService(SurveyRepository surveyRepository,
                         CampaignRepository campaignRepository,
                         SurveyDtoMapper surveyDtoMapper,
                         @Qualifier("surveyHashids") Hashids surveyHashids, WorkspaceService workspaceService) {
        this.surveyRepository = surveyRepository;
        this.campaignRepository = campaignRepository;
        this.surveyDtoMapper = surveyDtoMapper;
        this.surveyHashids = surveyHashids;
        this.workspaceService = workspaceService;
    }

    @Transactional
    public Survey save(Survey survey) {
        return surveyRepository.save(survey);
    }

    @Transactional
    public SurveyDto saveDto(SurveyDto surveyDto, String workspaceId) {

        if (!workspaceService.currentUserCanModifyWorkspace(workspaceId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Survey survey = surveyDtoMapper.dtoToEntity(surveyDto);
        Workspace workspace = workspaceService.getEntityByHashid(workspaceId);
        survey.setWorkspace(workspace);
        surveyRepository.save(survey);
        return surveyDtoMapper.entityToDto(survey);
    }

    public Survey findSurveyById(long id) {
        String hashid = surveyHashids.encode(id);
        return surveyRepository.findById(id).orElseThrow(() -> new SurveyNotFoundException(hashid));
    }

    public Survey findSurveyById(String hashid) {
        long id = hashidToId(hashid);
        return findSurveyById(id);
    }

    public SurveyDto findSurveyByCampaignId(String hashid) {
        long campaignId = hashidToId(hashid);
        var campaign = campaignRepository
                .findById(campaignId).orElseThrow(() -> new CampaignNotFoundException(hashid));
        if (campaign.getSurvey() == null) {
            throw new SurveyNotFoundException(hashid);
        }
        return surveyDtoMapper.entityToDto(campaign.getSurvey());
    }

    public Page<SurveyListElementDto> getPublicSurveys(Pageable pageable) {
        return getSurveysFromCampaigns(campaignRepository.findPublicCampaigns(pageable));
    }

    private Page<SurveyListElementDto> getSurveysFromCampaigns(Page<Campaign> campaigns) {
        Page<Survey> surveys = campaigns.map(Campaign::getSurvey);

        return surveyDtoMapper.surveysToListDto(surveys);
    }

    @Transactional
    public void deleteSurveyById(String hashid) {
        long id = hashidToId(hashid);
        Campaign campaign = campaignRepository.findById(id)
            .orElseThrow(() -> new CampaignNotFoundException(hashid));

        Workspace workspace = campaign.getWorkspace();

        if (!workspaceService.currentUserCanModifyWorkspace(workspace)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        campaignRepository.deleteById(id);
    }

    private long hashidToId(String hashid) {
        long[] decode = surveyHashids.decode(hashid);
        if (decode.length == 0) {
            throw new SurveyNotFoundException(hashid);
        }
        return decode[0];
    }

}
