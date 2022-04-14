package com.roal.survey_engine.domain.survey.controller;

import com.roal.survey_engine.domain.survey.dto.survey.SurveyDto;
import com.roal.survey_engine.domain.survey.dto.survey.out.SurveyListElementDto;
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
@RequestMapping("api/surveys")
@Tag(name = "Survey", description = "Survey API")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @Operation(summary = "Find survey by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SurveyDto.class))),
            @ApiResponse(responseCode = "404", description = "Survey not found")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SurveyDto getSurveyById(@PathVariable @NotBlank String id) {
        return surveyService.findSurveyByCampaignId(id);
    }

    @Operation(summary = "Create new survey")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SurveyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Input")})
    @PostMapping(value = "/{workspaceId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public SurveyDto postSurvey(@PathVariable @NotBlank String workspaceId,
                                @NotNull @Valid @RequestBody SurveyDto survey) {
        return surveyService.saveDto(survey, workspaceId);
    }

    @Operation(summary = "Find all surveys")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<SurveyListElementDto> getPublicSurveys(Pageable pageable) {
        return surveyService.getPublicSurveys(pageable);
    }

    @Operation(summary = "Delete Survey by ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{surveyId}")
    public void deleteSurvey(@PathVariable("surveyId") @NotBlank String id) {
        surveyService.deleteSurveyById(id);
    }
}
