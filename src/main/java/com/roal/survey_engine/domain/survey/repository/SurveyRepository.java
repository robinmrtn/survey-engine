package com.roal.survey_engine.domain.survey.repository;

import com.roal.survey_engine.domain.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    @Override
    @Query("update Survey s set s.deleted = true where s.id=:id")
    @Modifying
    void deleteById(Long id);
}
