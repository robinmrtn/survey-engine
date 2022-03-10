package com.roal.survey_engine.domain.reporting.dto.out;

import java.util.List;

public record CategoricalResponseAnalysisDto(long elementId, int count,
                                             List<CategoricalResponseAnalysisItemDto> items)
    implements AbstractElementReportingDto {
    @Override
    public String type() {
        return "cat";
    }
}
