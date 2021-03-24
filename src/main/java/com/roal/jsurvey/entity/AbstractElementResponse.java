package com.roal.jsurvey.entity;

import com.roal.jsurvey.entity.responses.SurveyResponse;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractElementResponse {

    @Id
    @GeneratedValue
    protected long id;

    @ManyToOne
    SurveyResponse surveyResponse;

//    @OneToOne(targetEntity = AbstractSurveyElement.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    protected AbstractSurveyElement element;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
