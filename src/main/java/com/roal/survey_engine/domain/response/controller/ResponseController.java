package com.roal.survey_engine.domain.response.controller;

import com.roal.survey_engine.domain.response.dto.SurveyResponseDto;
import com.roal.survey_engine.domain.response.service.ResponseService;
import com.roal.survey_engine.domain.survey.service.CampaignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/responses")
@Tag(name = "Response", description = "Response API")
public class ResponseController {

    private final ResponseService responseService;

    public ResponseController(ResponseService responseService, CampaignService campaignService) {
        this.responseService = responseService;
    }

    @Operation(summary = "Add a new response to a campaign")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SurveyResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Input")})
    @PostMapping(value = "/campaigns/{campaignId}", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public SurveyResponseDto postSurveyResponse(@PathVariable long campaignId,
                                                @RequestBody SurveyResponseDto surveyResponseDto) {
        return responseService.insertSurveyResponseDto(campaignId, surveyResponseDto);
    }

    @GetMapping("/campaigns/{campaignId}")
    public Page<SurveyResponseDto> getSurveyResponsesById(@PathVariable long campaignId, Pageable pageable) {
        return responseService.getResponsesByCampaignId(campaignId, pageable);
    }

    @Operation(summary = "Find Response by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful", content = @Content(schema = @Schema(implementation = SurveyResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Response not found")
    })
    @GetMapping("/{responseId}")
    public SurveyResponseDto getSurveyResponseById(@PathVariable long responseId) {
        return responseService.getResponseById(responseId);
    }
}
