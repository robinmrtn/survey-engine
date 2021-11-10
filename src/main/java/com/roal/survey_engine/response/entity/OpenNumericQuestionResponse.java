package com.roal.survey_engine.response.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roal.survey_engine.survey.entity.question.OpenNumericQuestion;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class OpenNumericQuestionResponse extends AbstractElementResponse {

    @JsonIgnore
    @OneToOne
    OpenNumericQuestion openNumericQuestion;

    public OpenNumericQuestionResponse() {
        // needed for hibernate
    }

    private double answer;

    public void setOpenNumericQuestion(OpenNumericQuestion openNumericQuestion) {
        this.openNumericQuestion = openNumericQuestion;
    }

    public double getAnswer() {
        return answer;
    }

    public void setAnswer(double answer) {
        this.answer = answer;
    }
}
