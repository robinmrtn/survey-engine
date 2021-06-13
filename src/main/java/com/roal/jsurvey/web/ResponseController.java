package com.roal.jsurvey.web;

import com.roal.jsurvey.dto.SurveyResponseDto;
import com.roal.jsurvey.service.CampaignService;
import com.roal.jsurvey.service.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/responses")
public class ResponseController {

    private final ResponseService responseService;

    private final CampaignService campaignService;

    public ResponseController(ResponseService responseService, CampaignService campaignService) {
        this.responseService = responseService;
        this.campaignService = campaignService;
    }

    @PostMapping("/campaigns/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void postSurveyResponse(@PathVariable long id, @RequestBody SurveyResponseDto surveyResponseDto) {
        var campaign = campaignService.findCampaignById(id);
        responseService.insertSurveyResponseDto(campaign, surveyResponseDto);
    }
}
