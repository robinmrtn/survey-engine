package com.roal.jsurvey.entity.responses;

import com.roal.jsurvey.entity.AbstractElementResponse;
import com.roal.jsurvey.entity.questions.ClosedQuestionAnswer;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class ClosedQuestionResponse extends AbstractElementResponse {

    @ManyToMany
    protected List<ClosedQuestionAnswer> answers;

    public ClosedQuestionResponse() {
    }

    public List<ClosedQuestionAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<ClosedQuestionAnswer> answers) {
        this.answers = answers;
    }
}
