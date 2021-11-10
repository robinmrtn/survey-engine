package com.roal.survey_engine.response.dto.mapping;

import com.roal.survey_engine.response.dto.ClosedQuestionResponseDto;
import com.roal.survey_engine.response.dto.ElementResponseDto;
import com.roal.survey_engine.response.dto.OpenNumericQuestionResponseDto;
import com.roal.survey_engine.response.dto.OpenQuestionResponseDto;

public class ResponseDtoMappingStrategyFactory {
    public static ResponseDtoMappingStrategy create(ElementResponseDto dto) {
        if (dto instanceof OpenQuestionResponseDto) {
            return new OpenTextQuestionResponseDtoMappingStrategy();
        } else if (dto instanceof ClosedQuestionResponseDto) {
            return new ClosedQuestionResponseDtoMappingStrategy();
        } else if (dto instanceof OpenNumericQuestionResponseDto) {
            return new OpenNumericQuestionResponseMappingStrategy();
        }

        throw new RuntimeException("Invalid ElementResponseDto type");
    }
}
