package com.roal.jsurvey.entity;


import java.util.Objects;

public abstract class AbstractSurveyElement {

    private long id;

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
