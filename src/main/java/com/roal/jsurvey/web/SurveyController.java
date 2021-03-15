package com.roal.jsurvey.web;

import com.roal.jsurvey.entity.Survey;
import com.roal.jsurvey.service.SurveyService;
import org.springframework.boot.WebApplicationType;
import org.springframework.http.MediaType;
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
    public Survey getSurveyById(@PathVariable long id) {
        //return new Survey("Test 3");
        return surveyService.getSurveyById(1L).orElse(new Survey("this is not a survey"));
    }

}
