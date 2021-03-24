package com.roal.jsurvey.entity.responses;

import com.roal.jsurvey.entity.AbstractElementResponse;
import com.roal.jsurvey.entity.questions.OpenQuestion;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class OpenQuestionResponse extends AbstractElementResponse {


    String value;

    @OneToOne
    OpenQuestion openQuestion;

    public OpenQuestionResponse() {
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

    public void setElement(OpenQuestion openQuestion) {
    }
}
