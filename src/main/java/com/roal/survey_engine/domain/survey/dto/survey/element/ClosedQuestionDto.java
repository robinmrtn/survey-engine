package com.roal.survey_engine.domain.survey.dto.survey.element;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public record ClosedQuestionDto(@NotNull String question, long id, int position,
                                @NotEmpty Set<ClosedQuestionAnswerDto> answers)
        implements AbstractElementDto {

    @Override
    public String type() {
        return "clq";
    }
}
