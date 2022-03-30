package com.roal.survey_engine.domain.response.repository;


import com.roal.survey_engine.domain.response.entity.OpenTextQuestionResponse;
import com.roal.survey_engine.domain.response.entity.SurveyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResponseRepository extends JpaRepository<SurveyResponse, Long> {

    @Query("select r from SurveyResponse r where r.survey.id =:id")
    List<SurveyResponse> findAllBySurveyId(long id);

    @Query("select r from SurveyResponse r where r.campaign.id =:id and r.deleted = false ")
    Page<SurveyResponse> findAllByCampaignId(long id, Pageable pageable);

    @Override
    @Query("update SurveyResponse sr set sr.deleted = true where sr.id =:id")
    @Modifying
    void deleteById(Long id);

    @Query("Select otqr from OpenTextQuestionResponse otqr join otqr.surveyResponse sr where sr.campaign.id =:id")
    Page<OpenTextQuestionResponse> findOpenTextQuestionResponseByCampaignId(long id, Pageable pageable);

    @Query("Select onqr.answer from OpenNumericQuestionResponse onqr join onqr.surveyResponse sr " +
            "where sr.campaign.id =:campaignId and onqr.openNumericQuestion.id =:elementId")
    List<Double> findOpenNumericQuestionResponseAnswersByElementIdAndCampaignId(long campaignId, long elementId);
}
