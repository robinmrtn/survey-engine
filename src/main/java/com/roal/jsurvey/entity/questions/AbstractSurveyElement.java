package com.roal.jsurvey.entity.questions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roal.jsurvey.entity.survey.SurveyPage;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractSurveyElement implements Comparable<AbstractSurveyElement> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "element_seq")
    protected long id;

    protected int position;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    protected SurveyPage surveyPage;

    public SurveyPage getSurveyPage() {
        return surveyPage;
    }

    public void setSurveyPage(SurveyPage surveyPage) {
        this.surveyPage = surveyPage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public abstract AbstractSurveyElement setPosition(int position);


    @Override
    public int compareTo(AbstractSurveyElement o) {
        return Integer.compare(this.position, o.getPosition());
    }


}
