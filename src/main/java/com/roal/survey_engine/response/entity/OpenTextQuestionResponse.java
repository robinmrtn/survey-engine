package com.roal.survey_engine.response.entity;

import com.roal.survey_engine.survey.entity.question.OpenTextQuestion;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class OpenTextQuestionResponse extends AbstractElementResponse {

    @OneToOne
    OpenTextQuestion openQuestion;

    private String answer;

    public OpenTextQuestionResponse() {
        // needed by hibernate
    }

    public OpenTextQuestion getOpenQuestion() {
        return openQuestion;
    }

    public OpenTextQuestionResponse setOpenQuestion(OpenTextQuestion openQuestion) {
        this.openQuestion = openQuestion;
        return this;
    }

    public String getAnswer() {
        return answer;
    }

    public OpenTextQuestionResponse setAnswer(String answer) {
        this.answer = answer;
        return this;
    }
}
