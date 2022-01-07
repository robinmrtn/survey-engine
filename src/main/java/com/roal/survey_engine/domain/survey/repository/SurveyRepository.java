package com.roal.survey_engine.domain.survey.repository;

import com.roal.survey_engine.domain.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
