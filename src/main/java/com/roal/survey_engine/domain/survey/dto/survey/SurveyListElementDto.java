package com.roal.survey_engine.domain.survey.dto.survey;

import com.roal.survey_engine.domain.survey.entity.Campaign;
import com.roal.survey_engine.domain.survey.entity.Survey;

import java.util.Objects;

public class SurveyListElementDto {

    private long id;
    private String name;
    private String description;

    public SurveyListElementDto(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static SurveyListElementDto fromEntity(Campaign campaign) {
        return fromEntity(campaign.getSurvey());
    }

    public static SurveyListElementDto fromEntity(Survey survey) {
        return new SurveyListElementDto(survey.getId(), survey.getTitle(), survey.getDescription());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SurveyListElementDto that = (SurveyListElementDto) o;

        if (id != that.id) return false;
        if (!Objects.equals(name, that.name)) return false;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
