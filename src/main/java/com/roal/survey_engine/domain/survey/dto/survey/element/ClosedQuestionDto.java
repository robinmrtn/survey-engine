package com.roal.survey_engine.domain.survey.dto.survey.element;

import java.util.Set;

public record ClosedQuestionDto(String question, long id, int position, Set<ClosedQuestionAnswerDto> answers)
        implements AbstractElementDto {

    @Override
    public String type() {
        return "clq";
    }
}
