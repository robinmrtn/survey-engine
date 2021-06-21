package com.roal.survey_engine.repository;


import com.roal.survey_engine.entity.response.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<SurveyResponse, Long> {
    List<SurveyResponse> findAllBySurveyId(long id);

    List<SurveyResponse> findAllByCampaignId(long id);
}
