package com.roal.jsurvey.entity.questions;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ClosedQuestionAnswer {

    String value;
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    private ClosedQuestion closedQuestion;
}
