package com.roal.survey_engine.entity.response;

import com.roal.survey_engine.entity.question.OpenNumericQuestion;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class OpenNumericQuestionResponse extends AbstractOpenQuestionResponse<Double> {

    @OneToOne
    OpenNumericQuestion openNumericQuestion;

    public OpenNumericQuestionResponse() {
        // needed for hibernate
    }

    public void setOpenNumericQuestion(OpenNumericQuestion openNumericQuestion) {
        this.openNumericQuestion = openNumericQuestion;
    }
}
