package com.roal.jsurvey.repository;

import com.roal.jsurvey.entity.AbstractSurveyElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyElementRepository extends JpaRepository<AbstractSurveyElement, Long> {
}
