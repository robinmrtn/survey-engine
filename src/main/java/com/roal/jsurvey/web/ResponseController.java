package com.roal.jsurvey.web;

import com.roal.jsurvey.dto.SurveyResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/response")
public class ResponseController {

    @PostMapping("/survey/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void postSurveyResponse(@PathVariable long id, @RequestBody SurveyResponseDto surveyResponseDto) {

    }
}
