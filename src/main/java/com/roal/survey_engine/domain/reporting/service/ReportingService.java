package com.roal.survey_engine.domain.reporting.service;

import com.roal.survey_engine.domain.reporting.dto.out.NumericReportingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReportingService {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    public NumericReportingDto getNumericReportingDtoByElementId(long elementId) {

        Integer count = jdbcTemplate.queryForObject("SELECT count(*) FROM open_numeric_question where id =:id",
                new MapSqlParameterSource("id", elementId), Integer.class);

        if (count == null || count == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return jdbcTemplate.queryForObject("SELECT COUNT(answer) as count, MIN(answer) as min, " +
                        "MAX(answer) as max, AVG(answer) as avg " +
                        "FROM open_numeric_question_response " +
                        "WHERE open_numeric_question_id=:id " +
                        "GROUP BY open_numeric_question_id",
                new MapSqlParameterSource("id", elementId),
                (rs, rowNum) -> new NumericReportingDto(elementId,
                        rs.getInt("count"),
                        rs.getDouble("avg"),
                        rs.getDouble("min"),
                        rs.getDouble("max"),
                        0,
                        0,
                        0,
                        0));


    }


}
