package com.roal.jsurvey.repository;


import com.roal.jsurvey.entity.responses.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseRepository extends JpaRepository<SurveyResponse, Long> {
}
