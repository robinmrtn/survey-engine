package com.roal.survey_engine.domain.reporting.dto.out;

public record NumericReportingDto(long elementId, int count, double avg, double min, double max,
                                  double median, double percentile25, double percentile75, double sd)
    implements AbstractElementReportingDto {
    @Override
    public String type() {
        return "num";
    }
}
