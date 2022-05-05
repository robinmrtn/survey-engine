package com.roal.survey_engine.domain.survey.dto.campaign;

import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.DateRange;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CampaignDtoMapper {

    private final Hashids campaignHashid;

    private final Hashids surveyHashid;

    public CampaignDtoMapper(@Qualifier("campaignHashids") Hashids campaignHashid,
                             @Qualifier("surveyHashids") Hashids surveyHashid) {
        this.campaignHashid = campaignHashid;
        this.surveyHashid = surveyHashid;
    }


    public CampaignDto entityToDto(Campaign campaign) {

        return new CampaignDto(campaignHashid.encode(campaign.getId()),
            campaign.getDateRange().getStartDate(),
            campaign.getDateRange().getEndDate(),
            campaign.getTitle(),
            campaign.isActive(),
            campaign.isHidden(),
            surveyHashid.encode(campaign.getSurvey().getId()));
    }

    public Campaign dtoToEntity(CreateCampaignDto campaignDto) {
        var dateRange = new DateRange(campaignDto.from(), campaignDto.to());
        return new Campaign()
            .setActive(campaignDto.active())
            .setHidden(campaignDto.hidden())
            .setTitle(campaignDto.title())
            .setDateRange(dateRange);
    }
}
