package com.roal.survey_engine.domain.reporting.dto.out;

public interface AbstractElementReportingDto {
    long elementId();

    int count();

    String type();
}
