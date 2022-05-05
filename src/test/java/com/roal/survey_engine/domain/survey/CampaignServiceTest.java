package com.roal.survey_engine.domain.survey;

import com.roal.survey_engine.domain.survey.dto.campaign.CampaignDto;
import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.DateRange;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.repository.CampaignRepository;
import com.roal.survey_engine.domain.survey.repository.SurveyRepository;
import com.roal.survey_engine.domain.survey.service.CampaignService;
import org.hashids.Hashids;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CampaignServiceTest {

    @Autowired
    CampaignService campaignService;

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    SurveyRepository surveyRepository;


    @Autowired
    @Qualifier("campaignHashids")
    Hashids campaignHashids;

    @Test
    void testFindCampaignDtoById() {

        Survey survey = surveyRepository.save(new Survey());

        Campaign campaign = new Campaign()
            .setDateRange(new DateRange(LocalDateTime.now(), LocalDateTime.MAX))
            .setSurvey(survey);

        campaignRepository.save(campaign);

        Long campaignId = campaign.getId();
        String hashid = campaignHashids.encode(campaignId);

        CampaignDto campaignDto = campaignService.findCampaignDtoById(hashid);

        assertEquals(hashid, campaignDto.id());
    }
}
