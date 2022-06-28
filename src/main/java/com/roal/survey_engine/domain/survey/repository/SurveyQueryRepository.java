package com.roal.survey_engine.domain.survey.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyQueryDto;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class SurveyQueryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    public SurveyQueryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    public String findSurveyById(String hashid) {
        String sql = "Select json from survey_json where survey_hashid=:hashid FETCH FIRST ROW ONLY ;";

        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("hashid", hashid), String.class);

    }

    @Transactional
    public void addSurvey(SurveyQueryDto surveyQueryDto, String campaignHashid) {
        String sql = "INSERT INTO survey_json (survey_hashid, campaign_hashid, json) " +
                "values (:hashid, :campaignHashid, :json) " +
                "on conflict (survey_hashid, campaign_hashid) do update " +
                "set survey_hashid = excluded.survey_hashid," +
                "   campaign_hashid = excluded.campaign_hashid," +
                "   created_at = current_timestamp; ";

        String json = null;
        try {
            json = objectMapper.writeValueAsString(surveyQueryDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("hashid", surveyQueryDto.id());
        params.addValue("campaignHashid", campaignHashid);
        params.addValue("json", json);
        jdbcTemplate.update(sql, params);
    }

    @Transactional
    public void addSurvey(SurveyQueryDto surveyQueryDto) {
        String sql = "INSERT INTO survey_json (survey_hashid, json) " +
                "values (:hashid, :json) " +
                "on conflict (survey_hashid, campaign_hashid) do update " +
                "set survey_hashid = excluded.survey_hashid," +
                " created_at = current_timestamp; ";


        String json = null;
        try {
            json = objectMapper.writeValueAsString(surveyQueryDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("hashid", surveyQueryDto.id());
        params.addValue("json", json);
        jdbcTemplate.update(sql, params);
    }

    @Transactional(readOnly = true)
    public String findSurveyByCampaignId(String hashid) {
        String sql = "Select json from survey_json  where campaign_hashid=:hashid;";

        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("hashid", hashid), String.class);

    }
}
