package com.roal.jsurvey.entity;

import com.roal.jsurvey.entity.survey.SurveyPage;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractSurveyElement {
    @Id
    @GeneratedValue
    protected long id;

    protected int position;

    @ManyToOne
    SurveyPage surveyPage;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractSurveyElement that = (AbstractSurveyElement) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
