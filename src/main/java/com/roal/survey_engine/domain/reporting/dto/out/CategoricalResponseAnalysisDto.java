package com.roal.survey_engine.domain.reporting.dto.out;

public record CategoricalResponseAnalysisDto(long elementId, int count) implements AbstractResponseAnalysisDto {
    @Override
    public String type() {
        return "cat";
    }
}
