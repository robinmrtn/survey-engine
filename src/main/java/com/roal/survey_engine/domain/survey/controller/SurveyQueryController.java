package com.roal.survey_engine.domain.survey.controller;

import com.roal.survey_engine.domain.survey.dto.survey.SurveyQueryDto;
import com.roal.survey_engine.domain.survey.service.SurveyQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class SurveyQueryController {

    private final SurveyQueryService surveyQueryService;

    public SurveyQueryController(SurveyQueryService surveyQueryService) {
        this.surveyQueryService = surveyQueryService;
    }

    @GetMapping(value = "/surveys/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Find survey by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SurveyQueryDto.class))),
            @ApiResponse(responseCode = "404", description = "Survey not found")
    })
    public SurveyQueryDto findBySurveyId(@PathVariable("id") String hashid) {
        return surveyQueryService.findSurveyById(hashid);
    }

//    @Operation(summary = "Find survey by Campaign ID")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successful",
//                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(implementation = SurveyQueryDto .class))),
//            @ApiResponse(responseCode = "404", description = "Survey not found")
//    })
//    @GetMapping(value = "campaigns/{id}/survey", produces = MediaType.APPLICATION_JSON_VALUE)
//    public SurveyQueryDto getSurveyByCampaignId(@PathVariable @NotBlank String id) {
//        return surveyQueryService.findSurveyByCampaignId(id);
//    }
}
