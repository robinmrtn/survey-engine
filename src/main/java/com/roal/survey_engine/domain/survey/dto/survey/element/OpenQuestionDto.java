package com.roal.survey_engine.domain.survey.dto.survey.element;

import javax.validation.constraints.NotBlank;

public record OpenQuestionDto(@NotBlank String question, int position, long id) implements AbstractElementDto {

    @Override
    public String type() {
        return "opq";
    }
}
