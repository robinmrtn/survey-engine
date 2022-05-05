package com.roal.survey_engine.domain.response.controller;

import com.roal.survey_engine.domain.response.dto.CreateSurveyResponseDto;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = SurveyResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid Input"),
        @ApiResponse(responseCode = "404", description = "Campaign not found")})
    @PostMapping(value = "/campaigns/{campaignId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public SurveyResponseDto createSurveyResponse(@PathVariable @NotBlank String campaignId,
                                                  @RequestBody @Valid @NotNull CreateSurveyResponseDto surveyResponseDto) {
        return responseService.create(campaignId, surveyResponseDto);
    }

    @Operation(summary = "Find responses for a campaign by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Page.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Campaign not found")
    })
    @GetMapping(value = "/campaigns/{campaignId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<SurveyResponseDto> getSurveyResponsesById(@PathVariable @NotBlank String campaignId,
                                                          Pageable pageable) {
        return responseService.getResponsesByCampaignId(campaignId, pageable);
    }

    @Operation(summary = "Find response by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SurveyResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Response not found")
    })
    @GetMapping(value = "/{responseId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SurveyResponseDto getSurveyResponseById(@PathVariable @NotBlank String responseId) {
        return responseService.getResponseById(responseId);
    }

    @DeleteMapping(value = "/{responseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSurveyResponse(@PathVariable @NotBlank String responseId) {
        responseService.deleteResponseById(responseId);
    }
}
