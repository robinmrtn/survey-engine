package com.roal.survey_engine.domain.survey.exception;

public class CampaignNotFoundException extends RuntimeException {
    public CampaignNotFoundException(String id) {
        super("Campaign with id " + id + " not found");
    }

    public CampaignNotFoundException() {
        super("Campaign not found");
    }
}
