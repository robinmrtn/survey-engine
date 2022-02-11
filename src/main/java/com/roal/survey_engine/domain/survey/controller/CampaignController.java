package com.roal.survey_engine.domain.survey.controller;

import com.roal.survey_engine.domain.survey.dto.campaign.CampaignDto;
import com.roal.survey_engine.domain.survey.service.CampaignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Tag(name = "Campaign", description = "Campaign API")
@RestController("/api/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @Operation(summary = "Find Campaign by ID")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    CampaignDto getById(@PathVariable long id) {
        return campaignService.findCampaignDtoById(id);
    }

    @Operation(summary = "Create new Campaign")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    CampaignDto createCampaign(@NotNull @Valid @RequestBody CampaignDto campaignDto) {
        return campaignService.insertDto(campaignDto);
    }

    @Operation(summary = "Add Survey (referenced by ID) to Campaign by ID")
    @PostMapping(value = "/{campaignId}/surveys/{surveyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    CampaignDto createSurveyToCampaign(@PathVariable long surveyId, @PathVariable long campaignId) {
        return campaignService.addSurveyToCampaign(surveyId, campaignId);
    }

    @Operation(summary = "Update Campaign by ID")
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    CampaignDto put(@NotNull @Valid @RequestBody CampaignDto campaignDto, @PathVariable long id) {
        return campaignService.updateCampaign(campaignDto, id);
    }

    @Operation(summary = "Delete Campaign by ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void delete(@PathVariable long id) {
        campaignService.deleteCampaign(id);
    }
}
