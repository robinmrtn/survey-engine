package com.roal.jsurvey.entity.responses;

import com.roal.jsurvey.entity.questions.ClosedQuestion;
import com.roal.jsurvey.entity.questions.ClosedQuestionAnswer;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
public class ClosedQuestionResponse extends AbstractElementResponse {

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

    public void setAnswers(List<ClosedQuestionAnswer> answers) {
        this.answers = answers;
    }

    public ClosedQuestion getClosedQuestion() {
        return closedQuestion;
    }

    public void setClosedQuestion(ClosedQuestion closedQuestion) {
        this.closedQuestion = closedQuestion;
    }
}
