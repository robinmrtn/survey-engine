package com.roal.survey_engine.domain.survey.dto.survey.element;

import javax.validation.constraints.NotBlank;

public record ClosedQuestionAnswerDto(@NotBlank String answer, long id) {
}
