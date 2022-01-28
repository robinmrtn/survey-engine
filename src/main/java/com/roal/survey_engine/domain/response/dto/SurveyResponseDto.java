package com.roal.survey_engine.domain.response.dto;

import java.util.List;

public class SurveyResponseDto {

    private long id;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
