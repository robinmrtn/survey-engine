package com.roal.survey_engine.response.dto;

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

    public void setValue(double value) {
        this.value = value;
    }
}
