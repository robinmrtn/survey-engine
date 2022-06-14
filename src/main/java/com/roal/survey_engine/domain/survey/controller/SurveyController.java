package com.roal.survey_engine.domain.survey.controller;

import com.roal.survey_engine.domain.survey.dto.survey.CreateSurveyDto;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyDto;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyListElementDto;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyQueryDto;
import com.roal.survey_engine.domain.survey.service.SurveyQuery;
import com.roal.survey_engine.domain.survey.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("api")
@Tag(name = "Survey", description = "Survey API")
public class SurveyController {

    private final SurveyService surveyService;

    private final SurveyQuery surveyQuery;

    public SurveyController(SurveyService surveyService, SurveyQuery surveyQuery) {
        this.surveyService = surveyService;
        this.surveyQuery = surveyQuery;
    }

    @Operation(summary = "Find survey by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SurveyQueryDto.class))),
            @ApiResponse(responseCode = "404", description = "Survey not found")
    })
    @GetMapping(value = "/surveys/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SurveyQueryDto getSurveyById(@PathVariable @NotBlank String id) {
        return surveyQuery.findSurveyById(id);
    }

    @Operation(summary = "Find survey by Campaign ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SurveyDto.class))),
            @ApiResponse(responseCode = "404", description = "Survey not found")
    })
    @GetMapping(value = "campaigns/{id}/survey", produces = MediaType.APPLICATION_JSON_VALUE)
    public SurveyDto getSurveyByCampaignId(@PathVariable @NotBlank String id) {
        return surveyService.findSurveyByCampaignId(id);
    }

    @Operation(summary = "Create new survey")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SurveyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Input")})
    @PostMapping(value = "/workspaces/{workspaceId}/surveys", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public SurveyDto postSurvey(@PathVariable @NotBlank String workspaceId,
                                @NotNull @Valid @RequestBody CreateSurveyDto survey) {
        return surveyService.create(survey, workspaceId);
    }

    @Operation(summary = "Find all surveys")
    @GetMapping(value = "/surveys", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<SurveyListElementDto> getPublicSurveys(Pageable pageable) {
        return surveyService.getPublicSurveys(pageable);
    }

    @Operation(summary = "Delete Survey by ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("surveys/{surveyId}")
    public void deleteSurvey(@PathVariable("surveyId") @NotBlank String id) {
        surveyService.deleteSurveyById(id);
    }
}
