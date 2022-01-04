package com.roal.survey_engine.domain.response.entity;

import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestion;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestionAnswer;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
public class ClosedQuestionResponse extends AbstractElementResponse{

    @ManyToMany
    protected List<ClosedQuestionAnswer> answers;

    @OneToOne
    private ClosedQuestion closedQuestion;

    public ClosedQuestionResponse() {
        // needed by hibernate
    }


    public List<ClosedQuestionAnswer> getAnswers() {
        return answers;
    }

    public ClosedQuestionResponse setAnswers(List<ClosedQuestionAnswer> answers) {
        this.answers = answers;
        return this;
    }

    public ClosedQuestion getClosedQuestion() {
        return closedQuestion;
    }

    public ClosedQuestionResponse setClosedQuestion(ClosedQuestion closedQuestion) {
        this.closedQuestion = closedQuestion;
        return this;
    }
}
