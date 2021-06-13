package com.roal.survey_engine.repository;

import com.roal.survey_engine.entity.survey.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    Optional<Survey> findById(long id);

}
