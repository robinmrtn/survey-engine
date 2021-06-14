package com.roal.survey_engine.entity.response;

import com.roal.survey_engine.entity.question.OpenTextQuestion;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class OpenTextQuestionResponse extends AbstractOpenQuestionResponse<String> {

    @OneToOne
    OpenTextQuestion openQuestion;

    public OpenTextQuestionResponse() {
        // needed by hibernate
    }

    public OpenTextQuestion getOpenQuestion() {
        return openQuestion;
    }

    public void setOpenQuestion(OpenTextQuestion openQuestion) {
        this.openQuestion = openQuestion;
    }
}
