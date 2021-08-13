package com.roal.survey_engine.entity.survey;

import javax.persistence.*;

@Entity
public class Campaign {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false)
    private Survey survey;

    @Embedded
    private DateRange dateRange;

    private String title;

    private boolean active;

    private boolean hidden;

    public Campaign() {
        // needed by hibernate
    }

    public Campaign setId(long id) {
        this.id = id;
        return this;
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

    public Campaign setActive(boolean active) {
        this.active = active;
        return this;
    }

    public Campaign setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Campaign campaign = (Campaign) o;

        return id == campaign.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
