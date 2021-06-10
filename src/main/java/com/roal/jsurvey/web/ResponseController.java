package com.roal.jsurvey.web;

import com.roal.jsurvey.dto.SurveyResponseDto;
import com.roal.jsurvey.service.ResponseService;
import com.roal.jsurvey.service.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/responses")
public class ResponseController {

    private final ResponseService responseService;

    private final SurveyService surveyService;

    public ResponseController(ResponseService responseService, SurveyService surveyService) {
        this.responseService = responseService;
        this.surveyService = surveyService;
    }

    @PostMapping("/surveys/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void postSurveyResponse(@PathVariable long id, @RequestBody SurveyResponseDto surveyResponseDto) {
        var survey = surveyService.findSurveyById(id);
        responseService.insertSurveyResponseDto(survey, surveyResponseDto);
    }
}
