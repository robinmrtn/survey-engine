package com.roal.jsurvey.entity.responses;

import com.roal.jsurvey.entity.questions.OpenQuestion;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class OpenQuestionResponse extends AbstractElementResponse {

    String value;

    @OneToOne
    OpenQuestion openQuestion;

    public OpenQuestionResponse() {
        // needed by hibernate
    }

    public OpenQuestionResponse(String value, OpenQuestion openQuestion) {
        this.value = value;
        this.openQuestion = openQuestion;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public OpenQuestion getOpenQuestion() {
        return openQuestion;
    }

    public void setOpenQuestion(OpenQuestion openQuestion) {
        this.openQuestion = openQuestion;
    }
}
