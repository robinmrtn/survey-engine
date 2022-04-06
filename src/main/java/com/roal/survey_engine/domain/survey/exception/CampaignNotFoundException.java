package com.roal.survey_engine.domain.survey.exception;

public class CampaignNotFoundException extends RuntimeException {
    public CampaignNotFoundException(String id) {
        super("Survey with id " + id + " not found");
    }

    public CampaignNotFoundException() {
        super("Campaign not found");
    }
}
