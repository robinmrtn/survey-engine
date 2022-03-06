package com.roal.survey_engine.domain.reporting.dto.out;

public interface AbstractResponseAnalysisDto {
    long elementId();

    int count();

    String type();
}
