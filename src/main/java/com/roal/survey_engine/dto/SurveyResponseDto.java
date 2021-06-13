package com.roal.survey_engine.dto;

import java.util.List;

public class SurveyResponseDto {

    private List<ElementResponseDto> elementResponseDtos;

    public SurveyResponseDto() {
    }

    public SurveyResponseDto(List<ElementResponseDto> elementResponseDtos) {
        this.elementResponseDtos = elementResponseDtos;
    }

    public List<ElementResponseDto> getElementResponseDtos() {
        return elementResponseDtos;
    }

    public void setElementResponseDtos(List<ElementResponseDto> elementResponseDtos) {
        this.elementResponseDtos = elementResponseDtos;
    }


}
