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

    public void setOpenNumericQuestion(OpenNumericQuestion openNumericQuestion) {
        this.openNumericQuestion = openNumericQuestion;
    }

    public double getAnswer() {
        return answer;
    }

    public void setAnswer(double answer) {
        this.answer = answer;
    }

    public OpenNumericQuestion getOpenNumericQuestion() {
        return openNumericQuestion;
    }
}
