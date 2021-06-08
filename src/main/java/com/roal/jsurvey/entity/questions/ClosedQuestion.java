package com.roal.jsurvey.entity.questions;

import com.roal.jsurvey.entity.AbstractSurveyElement;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;

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

    public ClosedQuestion(long id, String question) {
        this.id = id;
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<ClosedQuestionAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<ClosedQuestionAnswer> answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return id == ((ClosedQuestion) o).getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
