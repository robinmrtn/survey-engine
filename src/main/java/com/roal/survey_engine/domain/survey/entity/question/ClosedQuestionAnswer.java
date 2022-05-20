package com.roal.survey_engine.domain.survey.entity.question;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class ClosedQuestionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_closed_question_answer")
    @SequenceGenerator(name = "seq_closed_question_answer")
    private Long id;

    private String value;

    public ClosedQuestionAnswer() {
        // needed by hibernate
    }

    public ClosedQuestionAnswer(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public ClosedQuestionAnswer(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public ClosedQuestionAnswer setId(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (id == null) return false;
        ClosedQuestionAnswer that = (ClosedQuestionAnswer) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
