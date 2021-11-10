package com.roal.survey_engine.survey.controller;

import com.roal.survey_engine.survey.dto.SurveyListElementDto;
import com.roal.survey_engine.survey.entity.Survey;
import com.roal.survey_engine.survey.service.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/surveys")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping("/{id}")
    public Survey getSurveyById(@PathVariable long id) {
        return surveyService.findSurveyByCampaignId(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Survey postSurvey(@RequestBody Survey survey) {
        return surveyService.save(survey);
    }

    @GetMapping("")
    public List<SurveyListElementDto> getPublicSurveys() {
        return surveyService.getPublicAndActiveSurveys();
    }
}