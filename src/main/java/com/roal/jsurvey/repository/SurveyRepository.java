package com.roal.jsurvey.repository;

import com.roal.jsurvey.entity.Survey;

import java.util.Optional;

public interface SurveyRepository {
    Optional<Survey> findById(long id);
}
