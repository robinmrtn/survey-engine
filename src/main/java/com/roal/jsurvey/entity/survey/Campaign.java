package com.roal.jsurvey.entity.survey;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Campaign {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Survey survey;

    private DateRange dateRange;

    private boolean active;

    public Campaign() {
        // needed by hibernate
    }

    public Survey getSurvey() {
        return survey;
    }

    public Campaign setSurvey(Survey survey) {
        this.survey = survey;
        return this;
    }

    public long getId() {
        return id;
    }
}
