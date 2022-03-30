package com.roal.survey_engine.domain.reporting.controller;

import com.roal.survey_engine.domain.reporting.dto.out.AbstractElementReportingDto;
import com.roal.survey_engine.domain.reporting.service.ReportingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("campaigns/{campaignId}/report/{elementId}")
    AbstractElementReportingDto getNumericReportingDto(@PathVariable long campaignId, @PathVariable long elementId) {
        return reportingService.getNumericReportingDto(elementId, campaignId);
    }

    @GetMapping("campaigns/{campaignId}/report")
    List<AbstractElementReportingDto> getReportByCampaign(@PathVariable long campaignId) {
        return reportingService.getReportsByCampaignId(campaignId);
    }
}
