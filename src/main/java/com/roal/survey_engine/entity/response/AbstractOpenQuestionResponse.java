package com.roal.survey_engine.entity.response;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractOpenQuestionResponse<T> extends AbstractElementResponse {

    protected T answer;

    public T getAnswer() {
        return answer;
    }

    public void setAnswer(T answer) {
        this.answer = answer;
    }
}
