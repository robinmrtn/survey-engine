package com.roal.survey_engine.domain.survey.entity.question;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class ClosedQuestion extends AbstractSurveyElement {

    private String question;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "closed_question_id")
    @OrderBy("position")
    private List<ClosedQuestionAnswer> answers = new ArrayList<ClosedQuestionAnswer>();

    public List<ClosedQuestionAnswer> getAnswers() {
        return answers;
    }

    public ClosedQuestion() {
        // needed by hibernate
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

    public ClosedQuestion setAnswers(List<ClosedQuestionAnswer> answers) {
        this.answers = answers;
        return this;
    }

    public ClosedQuestion addAnswer(ClosedQuestionAnswer answer) {
        answers.add(answer);
//        answer.setClosedQuestion(this);
        return this;
    }

    @Override
    public ClosedQuestion setPosition(int position) {
        this.position = position;
        return this;
    }

    public static final class ClosedQuestionBuilder {

        private ClosedQuestionAnswer.ClosedQuestionAnswerBuilder answerBuilder;

        private String question;
        private long id;

        public ClosedQuestionBuilder setQuestion(String question) {
            this.question = question;
            return this;
        }

        public ClosedQuestionBuilder setId(long id) {
            this.id = id;
            return this;
        }

        public ClosedQuestionAnswer.ClosedQuestionAnswerBuilder withAnswers() {
            this.answerBuilder = new ClosedQuestionAnswer.ClosedQuestionAnswerBuilder(this);
            return this.answerBuilder;
        }

        public ClosedQuestion build() {
            ClosedQuestion closedQuestion = new ClosedQuestion();
            closedQuestion.setQuestion(question);
            closedQuestion.setId(id);
            Set<ClosedQuestionAnswer> answers = this.answerBuilder.getAnswers();
            for (ClosedQuestionAnswer answer : answers) {
                closedQuestion.addAnswer(answer);
            }
            return closedQuestion;
        }
    }

}
