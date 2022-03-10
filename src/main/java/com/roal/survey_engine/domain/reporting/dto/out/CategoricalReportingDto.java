package com.roal.survey_engine.domain.reporting.dto.out;

import java.util.List;

public record CategoricalReportingDto(long elementId, int count,
                                      List<CategoricalReportingItemDto> items)
    implements AbstractElementReportingDto {
    @Override
    public String type() {
        return "cat";
    }
}
