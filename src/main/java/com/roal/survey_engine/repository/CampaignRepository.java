package com.roal.survey_engine.repository;

import com.roal.survey_engine.entity.survey.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
}
