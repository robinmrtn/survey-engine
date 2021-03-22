package com.roal.jsurvey.web;

import com.roal.jsurvey.entity.Survey;
import com.roal.jsurvey.service.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/survey")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping("/{id}")
    public Optional<Survey> getSurveyById(@PathVariable long id) {
        return surveyService.findSurveyById(id);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void postSurvey(@PathVariable long id) {
        surveyService.findSurveyById(id);

    }
}
