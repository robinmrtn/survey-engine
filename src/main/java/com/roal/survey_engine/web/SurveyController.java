package com.roal.survey_engine.web;

import com.roal.survey_engine.entity.survey.Survey;
import com.roal.survey_engine.service.SurveyService;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Survey postSurvey(@RequestBody Survey survey) {
        return surveyService.save(survey);
    }

    @GetMapping("/")
    public List<Survey> getPublicSurveys() {
        return surveyService.getPublicAndActiveSurveys();
    }
}
