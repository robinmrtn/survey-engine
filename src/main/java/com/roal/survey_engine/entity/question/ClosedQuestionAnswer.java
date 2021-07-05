package com.roal.survey_engine.entity.question;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ClosedQuestionAnswer {

    @Id
    @GeneratedValue
    private long id;

    @JsonIgnore
    @ManyToOne
    private ClosedQuestion closedQuestion;

    private String value;

    public ClosedQuestionAnswer() {
        // needed by hibernate
    }

    public ClosedQuestionAnswer(long id, String value) {
        this.id = id;
        this.value = value;
    }

    public ClosedQuestionAnswer(String value) {
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setClosedQuestion(ClosedQuestion closedQuestion) {
        this.closedQuestion = closedQuestion;
    }

    public static final class ClosedQuestionAnswerBuilder {

        private ClosedQuestion.ClosedQuestionBuilder closedQuestionBuilder;

        private List<ClosedQuestionAnswer> answers = new ArrayList<>();

        public ClosedQuestionAnswerBuilder(ClosedQuestion.ClosedQuestionBuilder closedQuestionBuilder) {
            this.closedQuestionBuilder = closedQuestionBuilder;
        }

        public List<ClosedQuestionAnswer> getAnswers() {
            return answers;
        }

        public ClosedQuestionAnswerBuilder addAnswer(ClosedQuestionAnswer answer) {
            this.answers.add(answer);
            return this;
        }

        public ClosedQuestion.ClosedQuestionBuilder build() {
            return closedQuestionBuilder;
        }
    }

}
