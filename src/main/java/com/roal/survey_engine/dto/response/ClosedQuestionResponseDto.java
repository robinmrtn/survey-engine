package com.roal.survey_engine.dto.response;

import java.util.Set;

public class ClosedQuestionResponseDto extends ElementResponseDto {

    private Set<Long> answers;

    public ClosedQuestionResponseDto(long elementId, Set<Long> answers) {
        super(elementId);
        this.answers = answers;
    }

    public ClosedQuestionResponseDto() {
    }

    public Set<Long> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Long> answers) {
        this.answers = answers;
    }
}
