package com.roal.survey_engine.domain.survey;

import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.repository.CampaignRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@DataJpaTest
public class CampaignJpaRepositoryTest {

    @Autowired
    private CampaignRepository campaignRepository;

    @BeforeEach
    void setup() {
        var privateCampaign = new Campaign().setHidden(true).setActive(true);
        var publicCampaign = new Campaign().setHidden(false).setActive(true);
        var deactivatedCampaign = new Campaign().setHidden(false).setActive(false);
        campaignRepository.save(privateCampaign);
        campaignRepository.save(publicCampaign);
        campaignRepository.save(deactivatedCampaign);
        campaignRepository.flush();
    }

    @Test
    void testFindPublicAndActive() {
        Page<Campaign> campaigns = campaignRepository.findByHiddenIsFalseAndActiveIsTrue(Pageable.unpaged());

        Assertions.assertEquals(1, campaigns.getTotalElements());
    }
}
