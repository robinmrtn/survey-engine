package com.roal.survey_engine.response.controller;

import com.roal.survey_engine.response.dto.SurveyResponseDto;
import com.roal.survey_engine.response.entity.SurveyResponse;
import com.roal.survey_engine.response.service.ResponseService;
import com.roal.survey_engine.survey.service.CampaignService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/responses")
public class ResponseController {

    private final ResponseService responseService;

    public ResponseController(ResponseService responseService, CampaignService campaignService) {
        this.responseService = responseService;
    }

    @PostMapping("/campaigns/{campaignId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void postSurveyResponse(@PathVariable long campaignId,
                                   @RequestBody SurveyResponseDto surveyResponseDto) {

        responseService.insertSurveyResponseDto(campaignId, surveyResponseDto);
    }

    @GetMapping("/campaigns/{campaignId}")
    public List<SurveyResponse> getSurveyResponsesById(@PathVariable long campaignId) {
        List<SurveyResponse> responsesByCampaignId = responseService.getResponsesByCampaignId(campaignId);
        return responsesByCampaignId;
    }

    @GetMapping("/{id}")
    public SurveyResponse getSurveyResponseById(@PathVariable long id) {
        SurveyResponse responseById = responseService.getResponseById(id);
        return responseById;
    }
}
