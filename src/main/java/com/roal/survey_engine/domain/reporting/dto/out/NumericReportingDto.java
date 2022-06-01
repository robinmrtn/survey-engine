package com.roal.survey_engine.domain.reporting.dto.out;

public record NumericReportingDto(long elementId, int count, double avg, double min, double max,
                                  PercentileReportingDto percentiles, double sd)
    implements ReportingDto {
    @Override
    public String type() {
        return "num";
    }
}
