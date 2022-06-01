package com.roal.survey_engine.domain.reporting.dto.out;

public record PercentileReportingDto(double percentile50, double percentile25, double percentile75) {
    public static PercentileReportingDto getDefault() {
        return new PercentileReportingDto(0.0, 0.0, 0.0);
    }
}
