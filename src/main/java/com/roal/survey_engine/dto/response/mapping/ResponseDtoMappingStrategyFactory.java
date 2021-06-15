package com.roal.survey_engine.dto.response.mapping;

import com.roal.survey_engine.dto.response.ClosedQuestionResponseDto;
import com.roal.survey_engine.dto.response.ElementResponseDto;
import com.roal.survey_engine.dto.response.OpenQuestionResponseDto;

public class ResponseDtoMappingStrategyFactory {
    public static ResponseDtoMappingStrategy create(ElementResponseDto dto) {
        if (dto instanceof OpenQuestionResponseDto) {
            return new OpenTextQuestionResponseDtoMappingStrategy();
        } else if (dto instanceof ClosedQuestionResponseDto) {
            return new ClosedQuestionResponseDtoMappingStrategy();
        }

        throw new RuntimeException("Invalid ElementResponseDto type");
    }
}
