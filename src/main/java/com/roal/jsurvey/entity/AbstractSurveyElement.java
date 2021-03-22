package com.roal.jsurvey.entity;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractSurveyElement {
    @Id
    @GeneratedValue
    protected long id;

    @OneToOne
    private SurveyPagePosition surveyPagePosition;

}
