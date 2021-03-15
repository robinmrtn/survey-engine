package com.roal.jsurvey.web;

import com.roal.jsurvey.entity.Survey;
import com.roal.jsurvey.service.SurveyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("survey")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping("/{id}")
    public Survey getSurveyById(@PathVariable long id) {
        return surveyService.getSurveyById(id).orElseThrow();
    }

}
