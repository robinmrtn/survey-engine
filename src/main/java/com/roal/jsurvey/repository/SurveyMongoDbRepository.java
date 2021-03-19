package com.roal.jsurvey.repository;

import com.roal.jsurvey.entity.Survey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyMongoDbRepository extends MongoRepository<Survey, String> {
}
