package com.roal.jsurvey.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractSurveyElement {
    @Id
    @GeneratedValue
    protected long id;

    @OneToOne(cascade = CascadeType.ALL)
    private SurveyPagePosition surveyPagePosition;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
