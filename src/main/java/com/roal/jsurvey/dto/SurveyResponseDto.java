package com.roal.jsurvey.dto;

import java.util.List;

public class SurveyResponseDto {

    private long surveyId;

    private List<ElementResponseDto> elementResponseDtos;

    public SurveyResponseDto() {
    }

    public SurveyResponseDto(long surveyId, List<ElementResponseDto> elementResponseDtos) {
        this.surveyId = surveyId;
        this.elementResponseDtos = elementResponseDtos;
    }

    public List<ElementResponseDto> getElementResponseDtos() {
        return elementResponseDtos;
    }

    public void setElementResponseDtos(List<ElementResponseDto> elementResponseDtos) {
        this.elementResponseDtos = elementResponseDtos;
    }

    public long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(long surveyId) {
        this.surveyId = surveyId;
    }


}
