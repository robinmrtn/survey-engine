package com.roal.survey_engine.domain.survey.dto.survey;

import javax.validation.constraints.NotBlank;
import java.util.List;

public record SurveyQueryDto(String id, @NotBlank String title, String description,
                             List<SurveyPageDto> surveyPages) {

    public static SurveyQueryDto from(SurveyDto surveyDto) {
        return new SurveyQueryDto(surveyDto.id(), surveyDto.title(), surveyDto.description(), surveyDto.surveyPages());
    }
}
