package com.roal.jsurvey.service;

import com.roal.jsurvey.entity.Survey;
import com.roal.jsurvey.repository.SurveyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;

    public SurveyService(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    public Optional<Survey> findSurveyById(long i) {
        return surveyRepository.findById(i);
    }
}
