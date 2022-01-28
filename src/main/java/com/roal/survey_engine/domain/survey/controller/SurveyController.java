package com.roal.survey_engine.domain.survey.controller;

import com.roal.survey_engine.domain.survey.dto.SurveyDto;
import com.roal.survey_engine.domain.survey.dto.SurveyListElementDto;
import com.roal.survey_engine.domain.survey.service.SurveyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/surveys")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping("/{id}")
    public SurveyDto getSurveyById(@PathVariable long id) {
        return surveyService.findSurveyByCampaignId(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public SurveyDto postSurvey(@RequestBody SurveyDto survey) {
        return surveyService.saveDto(survey);
    }

    @GetMapping("")
    public Page<SurveyListElementDto> getPublicSurveys(Pageable pageable) {
        return surveyService.getPublicAndActiveSurveys(pageable);
    }
}
