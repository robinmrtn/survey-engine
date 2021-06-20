package com.roal.survey_engine.entity.question;

import javax.persistence.Entity;

@Entity
public class OpenNumericQuestion extends AbstractOpenQuestion {

    public OpenNumericQuestion() {
        // needed by hibernate
    }

    public OpenNumericQuestion(long id, String question) {
        this.question = question;
        this.id = id;
    }

    @Override
    public OpenNumericQuestion setPosition(int position) {
        this.position = position;
        return this;
    }

}
