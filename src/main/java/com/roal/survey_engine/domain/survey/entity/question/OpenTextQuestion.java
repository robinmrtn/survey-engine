package com.roal.survey_engine.domain.survey.entity.question;

import javax.persistence.Entity;

@Entity
public class OpenTextQuestion extends AbstractOpenQuestion {

    public OpenTextQuestion() {
        // needed by hibernate
    }

    public OpenTextQuestion(String question) {
        this.question = question;
    }

    public OpenTextQuestion(long id, String question) {
        this.question = question;
        this.id = id;
    }

    @Override
    public OpenTextQuestion setPosition(int position) {
        this.position = position;
        return this;
    }
}
