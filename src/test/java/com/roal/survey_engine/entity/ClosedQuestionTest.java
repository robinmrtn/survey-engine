package com.roal.survey_engine.entity;

import com.roal.survey_engine.entity.question.ClosedQuestion;
import com.roal.survey_engine.entity.question.ClosedQuestionAnswer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClosedQuestionTest {

    @Test
    void testBuilder() {
        ClosedQuestion closedQuestion = new ClosedQuestion.ClosedQuestionBuilder()
                .setId(3)
                .setQuestion("This is an open question")
                .withAnswers()
                .addAnswer(new ClosedQuestionAnswer(21, "first answer"))
                .addAnswer(new ClosedQuestionAnswer(33, "second answer"))
                .build()
                .build();

        assertAll(() -> assertEquals(2, closedQuestion.getAnswers().size()),
                () -> assertEquals("second answer", closedQuestion.getAnswers().get(1).getValue()),
                () -> assertEquals("This is an open question", closedQuestion.getQuestion()),
                () -> assertEquals(3, closedQuestion.getId()));
    }
}
