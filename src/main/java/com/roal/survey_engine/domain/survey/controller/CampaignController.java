package com.roal.survey_engine.domain.survey.controller;

import com.roal.survey_engine.domain.survey.dto.campaign.CampaignDto;
import com.roal.survey_engine.domain.survey.service.CampaignService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Campaign", description = "Campaign API")
@RestController("/api/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    CampaignDto getById(@PathVariable long id) {
        return campaignService.findCampaignDtoById(id);
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    CampaignDto postCampaign(@RequestBody CampaignDto campaignDto) {
        return campaignService.insertDto(campaignDto);
    }

    @PostMapping(value = "/{campaignId}/surveys/{surveyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    CampaignDto postSurveyToCampaign(@PathVariable long surveyId, @PathVariable long campaignId) {
        return campaignService.addSurveyToCampaign(surveyId, campaignId);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    CampaignDto put(@RequestBody CampaignDto campaignDto, @PathVariable long id) {
        return campaignService.updateCampaign(campaignDto, id);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable long id) {
        campaignService.deleteCampaign(id);
    }
}
