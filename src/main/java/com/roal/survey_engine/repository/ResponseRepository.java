package com.roal.survey_engine.repository;


import com.roal.survey_engine.entity.responses.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseRepository extends JpaRepository<SurveyResponse, Long> {
}
