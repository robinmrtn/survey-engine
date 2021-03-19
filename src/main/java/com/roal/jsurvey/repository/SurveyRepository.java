package com.roal.jsurvey.repository;

import com.roal.jsurvey.entity.Survey;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface SurveyRepository extends MongoRepository<Survey, String> {
    Optional<Survey> findById(long id);

    Optional<Survey> findById(String id);

}
