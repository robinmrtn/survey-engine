package com.roal.survey_engine.entity.responses;

import com.roal.survey_engine.entity.questions.OpenTextQuestion;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class OpenTextQuestionResponse extends AbstractOpenQuestionResponse<String> {

    @OneToOne
    OpenTextQuestion openQuestion;

    public OpenTextQuestionResponse() {
        // needed by hibernate
    }

    public OpenTextQuestionResponse(String value, OpenTextQuestion openQuestion) {
        this.answer = value;
        this.openQuestion = openQuestion;
    }

    public OpenTextQuestion getOpenQuestion() {
        return openQuestion;
    }

    public void setOpenQuestion(OpenTextQuestion openQuestion) {
        this.openQuestion = openQuestion;
    }
}
