package com.roal.jsurvey.service;

import com.roal.jsurvey.entity.Survey;
import com.roal.jsurvey.repository.SurveyMongoDbRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SurveyService {

    private final SurveyMongoDbRepository surveyRepository;

    public SurveyService(SurveyMongoDbRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    public Optional<Survey> findSurveyById(String i) {
        return surveyRepository.findById(i);
    }

    public Optional<Survey> findSurveyById(long i) {
        return surveyRepository.findById(Long.toString(i));
    }
}
