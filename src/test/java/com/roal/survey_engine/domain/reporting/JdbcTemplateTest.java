package com.roal.survey_engine.domain.reporting;

import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.SurveyPage;
import com.roal.survey_engine.domain.survey.entity.question.OpenTextQuestion;
import com.roal.survey_engine.domain.survey.repository.SurveyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("dev-h2")
@DataJpaTest
public class JdbcTemplateTest {

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Test
    void JdbcTemplateWorksWithJpaData() {
        var survey = new Survey()
                .setTitle("title")
                .setDescription("Desc")
                .addSurveyPage(new SurveyPage()
                        .addSurveyElement(new OpenTextQuestion("question")));
        surveyRepository.saveAndFlush(survey);

        String sql = "Select count(*) from survey";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);

        Assertions.assertEquals(1, count);
    }
}
