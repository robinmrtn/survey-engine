package com.roal.survey_engine.entity.questions;

import javax.persistence.Entity;

@Entity
public class OpenNumericQuestion extends AbstractOpenQuestion {

    public OpenNumericQuestion() {
        // needed by hibernate
    }

    @Override
    public OpenNumericQuestion setPosition(int position) {
        this.position = position;
        return this;
    }

}
