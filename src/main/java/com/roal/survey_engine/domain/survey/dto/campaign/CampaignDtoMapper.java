package com.roal.survey_engine.domain.survey.dto.campaign;

import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.DateRange;
import org.springframework.stereotype.Component;

@Component
public class CampaignDtoMapper {

    public CampaignDto entityToDto(Campaign campaign) {

        return new CampaignDto(campaign.getId(),
            campaign.getDateRange().getStartDate(),
            campaign.getDateRange().getEndDate(),
            campaign.getTitle(),
            campaign.isActive(),
            campaign.isHidden(),
            campaign.getSurvey().getId());
    }

    public Campaign dtoToEntity(CampaignDto campaignDto) {
        var dateRange = new DateRange(campaignDto.from(), campaignDto.to());
        return new Campaign()
            .setActive(campaignDto.active())
            .setHidden(campaignDto.hidden())
            .setTitle(campaignDto.title())
            .setDateRange(dateRange);
    }
}
