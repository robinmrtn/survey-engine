package com.roal.survey_engine.domain.survey.repository;

import com.roal.survey_engine.domain.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    @Override
    @Query("update Survey s set s.deleted = true where s.id=:id")
    @Modifying
    void deleteById(Long id);

    @Override
    @Query("Select s from Survey s where s.deleted = false")
    List<Survey> findAll();

    @Override
    @Query("Select s from Survey s where s.id=:id and s.deleted = false")
    Optional<Survey> findById(Long id);
}
