package com.roal.survey_engine.domain.survey.service;

import com.roal.survey_engine.domain.survey.dto.survey.CreateSurveyDto;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyDto;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyDtoMapper;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyListElementDto;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.Workspace;
import com.roal.survey_engine.domain.survey.exception.SurveyNotFoundException;
import com.roal.survey_engine.domain.survey.repository.SurveyRepository;
import com.roal.survey_engine.domain.user.exception.ForbiddenUserActionException;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final SurveyDtoMapper surveyDtoMapper;
    private final Hashids surveyHashids;
    private final WorkspaceService workspaceService;
    private final CampaignService campaignService;
    private final SurveyQueryProducer surveyQueryProducer;

    public SurveyService(SurveyRepository surveyRepository,
                         SurveyDtoMapper surveyDtoMapper,
                         @Qualifier("surveyHashids") Hashids surveyHashids,
                         WorkspaceService workspaceService,
                         @Lazy CampaignService campaignService,
                         SurveyQueryProducer surveyQueryProducer) {
        this.surveyRepository = surveyRepository;
        this.surveyDtoMapper = surveyDtoMapper;
        this.surveyHashids = surveyHashids;
        this.workspaceService = workspaceService;
        this.campaignService = campaignService;
        this.surveyQueryProducer = surveyQueryProducer;
    }

    @Transactional
    public SurveyDto create(CreateSurveyDto surveyDto, String workspaceId) {

        if (!workspaceService.currentUserCanModifyWorkspace(workspaceId)) {
            throw new ForbiddenUserActionException();
        }

        Survey survey = surveyDtoMapper.dtoToEntity(surveyDto);
        Workspace workspace = workspaceService.getEntityByHashid(workspaceId);
        survey.setWorkspace(workspace);
        surveyRepository.save(survey);
        addToQuery(survey);
        return surveyDtoMapper.entityToDto(survey);
    }

    private void addToQuery(Survey survey) {
        SurveyDto surveyDto = surveyDtoMapper.entityToDto(survey);
        surveyQueryProducer.sendAddSurveyMessage(surveyDto);
    }

    @Transactional(readOnly = true)
    public Survey findSurveyById(long id) {
        String hashid = surveyHashids.encode(id);
        return surveyRepository.findById(id)
                .orElseThrow(() -> new SurveyNotFoundException(hashid));
    }

    @Transactional(readOnly = true)
    public Survey findSurveyById(String hashid) {
        long id = hashidToId(hashid);
        return findSurveyById(id);
    }

    @Transactional(readOnly = true)
    public SurveyDto findSurveyByCampaignId(String hashid) {
        Campaign campaignById = campaignService.findCampaignById(hashid);
        if (campaignById.getSurvey() == null) {
            throw new SurveyNotFoundException();
        }
        return surveyDtoMapper.entityToDto(campaignById.getSurvey());
    }

    @Transactional(readOnly = true)
    public SurveyDto findSurveyDtoById(String hashid) {
        long surveyId = hashidToId(hashid);
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new SurveyNotFoundException(hashid));

        return surveyDtoMapper.entityToDto(survey);
    }

    @Transactional(readOnly = true)
    public Page<SurveyListElementDto> getPublicSurveys(Pageable pageable) {
        return campaignService.findPublicCampaigns(pageable);
    }

    private Page<SurveyListElementDto> getSurveysFromCampaigns(Page<Campaign> campaigns) {
        Page<Survey> surveys = campaigns.map(Campaign::getSurvey);

        return surveyDtoMapper.surveysToListDto(surveys);
    }

    @Transactional
    public void deleteSurveyById(String hashid) {

        Campaign campaign = campaignService.findCampaignById(hashid);
        Workspace workspace = campaign.getWorkspace();

        if (!workspaceService.currentUserCanModifyWorkspace(workspace)) {
            throw new ForbiddenUserActionException();
        }
        campaignService.deleteCampaignById(hashid);
    }

    private long hashidToId(String hashid) {
        long[] decode = surveyHashids.decode(hashid);
        if (decode.length == 0) {
            throw new SurveyNotFoundException(hashid);
        }
        return decode[0];
    }
}
