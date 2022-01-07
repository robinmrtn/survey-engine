package com.roal.survey_engine.domain.response.repository;


import com.roal.survey_engine.domain.response.entity.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResponseRepository extends JpaRepository<SurveyResponse, Long> {

    @Query("select r from SurveyResponse r where r.survey.id =:id")
    List<SurveyResponse> findAllBySurveyId(long id);

    @Query("select r from SurveyResponse r where r.campaign.id =:id")
    List<SurveyResponse> findAllByCampaignId(long id);
}
