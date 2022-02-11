package com.roal.survey_engine.domain.survey.repository;

import com.roal.survey_engine.domain.survey.entity.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    @Query("select c from Campaign c where c.hidden = false and c.active = true and c.deleted = false ")
    Page<Campaign> findPublicCampaigns(Pageable pageable);

    @Override
    @Query("update Campaign c set c.deleted = true where c.id =:id")
    void deleteById(Long id);
}
