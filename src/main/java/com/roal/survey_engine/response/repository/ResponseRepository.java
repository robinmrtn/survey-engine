package com.roal.survey_engine.response.repository;


import com.roal.survey_engine.response.entity.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<SurveyResponse, Long> {
    List<SurveyResponse> findAllBySurveyId(long id);

    @Query("select r from SurveyResponse r where r.campaign.id =:id")
    List<SurveyResponse> findAllByCampaignId(long id);
}
