package com.roal.survey_engine.domain.response.dto;

import javax.validation.constraints.NotBlank;

public class OpenQuestionResponseDto extends ElementResponseDto {

    @NotBlank
    private String value;

    public OpenQuestionResponseDto(long id, String value) {
        super(id);
        this.value = value;
    }

    public OpenQuestionResponseDto() {

    }

    public String getValue() {
        return value;
    }

    public OpenQuestionResponseDto setValue(String value) {
        this.value = value;
        return this;
    }

}
