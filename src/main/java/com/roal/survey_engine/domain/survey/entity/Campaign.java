package com.roal.survey_engine.domain.survey.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_campaign")
    @SequenceGenerator(name = "seq_campaign")
    private Long id;

    @ManyToOne
    private Survey survey;

    @Embedded
    private DateRange dateRange;

    private String title;

    private boolean active;

    private boolean hidden;

    private boolean deleted = Boolean.FALSE;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Campaign() {
        // needed by hibernate
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public Campaign setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
        return this;
    }

    public Campaign setId(long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Campaign setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isHidden() {
        return hidden;
    }

    public Survey getSurvey() {
        return survey;
    }

    public Campaign setSurvey(Survey survey) {
        this.survey = survey;
        return this;
    }

    public Long getId() {
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

    public Workspace getWorkspace() {
        return survey.getWorkspace();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (id == null) return false;
        Campaign campaign = (Campaign) o;

        return id.equals(campaign.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
