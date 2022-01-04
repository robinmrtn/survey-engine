package com.roal.survey_engine.domain.response.dto;

public class OpenNumericQuestionResponseDto extends ElementResponseDto {

    double value;

    public OpenNumericQuestionResponseDto() {
    }

    public OpenNumericQuestionResponseDto(long elementId, double value) {
        super(elementId);
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public OpenNumericQuestionResponseDto setValue(double value) {
        this.value = value;
        return this;
    }
}
