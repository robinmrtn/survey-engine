package com.roal.jsurvey.service;

import com.roal.jsurvey.entity.Survey;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SurveyService {
    public Optional<Survey> getSurveyById(long i) {
        return Optional.of(new Survey("Test"));
    }
}
