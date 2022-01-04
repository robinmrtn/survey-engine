package com.roal.survey_engine.domain.response.entity;

//@MappedSuperclass
public abstract class AbstractOpenQuestionResponse<T> extends AbstractElementResponse {

    protected T answer;

    public T getAnswer() {
        return answer;
    }

    public void setAnswer(T answer) {
        this.answer = answer;
    }
}
