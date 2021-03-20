package com.roal.jsurvey.entity;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractSurveyElement {
    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    private SurveyPagePosition surveyPagePosition;

}
