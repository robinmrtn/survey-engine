package com.roal.jsurvey.entity.questions;

import com.roal.jsurvey.entity.AbstractSurveyElement;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;


@Entity
public class ClosedQuestion extends AbstractSurveyElement {

    private String question;

    @OneToMany(mappedBy = "closedQuestion")
    private List<ClosedQuestionAnswer> answers;

    public ClosedQuestion() {
    }


    public ClosedQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }


}
