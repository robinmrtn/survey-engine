package com.roal.survey_engine.domain.response.entity;

import com.roal.survey_engine.domain.survey.entity.question.OpenNumericQuestion;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class OpenNumericQuestionResponse extends AbstractElementResponse {


    @OneToOne
    private OpenNumericQuestion openNumericQuestion;

    public OpenNumericQuestionResponse() {
        // needed for hibernate
    }

    private double answer;

    public OpenNumericQuestionResponse setOpenNumericQuestion(OpenNumericQuestion openNumericQuestion) {
        this.openNumericQuestion = openNumericQuestion;
        return this;
    }

    public double getAnswer() {
        return answer;
    }

    public OpenNumericQuestionResponse setAnswer(double answer) {
        this.answer = answer;
        return this;
    }

    public OpenNumericQuestion getOpenNumericQuestion() {
        return openNumericQuestion;
    }
}
