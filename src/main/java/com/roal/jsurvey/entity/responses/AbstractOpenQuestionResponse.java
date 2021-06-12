package com.roal.jsurvey.entity.responses;

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
