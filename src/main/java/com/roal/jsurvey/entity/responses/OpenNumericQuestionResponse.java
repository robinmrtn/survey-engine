package com.roal.jsurvey.entity.responses;

import com.roal.jsurvey.entity.questions.OpenNumericQuestion;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class OpenNumericQuestionResponse extends AbstractOpenQuestionResponse<Double> {

    @OneToOne
    OpenNumericQuestion openNumericQuestion;

    public OpenNumericQuestionResponse() {
        // needed for hibernate
    }
}
