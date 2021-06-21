package com.roal.survey_engine.entity.survey;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class Campaign {

    @Id
    @GeneratedValue
    private long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Campaign)) return false;
        Campaign campaign = (Campaign) o;
        return getId() == campaign.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @ManyToOne
    private Survey survey;

    private DateRange dateRange;

    private String title;

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
