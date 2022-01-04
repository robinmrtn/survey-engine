package com.roal.survey_engine.domain.response.dto.mapping;

import com.roal.survey_engine.domain.response.dto.ClosedQuestionResponseDto;
import com.roal.survey_engine.domain.response.dto.ElementResponseDto;
import com.roal.survey_engine.domain.response.dto.OpenNumericQuestionResponseDto;
import com.roal.survey_engine.domain.response.dto.OpenQuestionResponseDto;

public class ResponseDtoMappingStrategyFactory {

    private final static OpenNumericQuestionResponseMappingStrategy openNumericQuestionResponseMappingStrategy =
            new OpenNumericQuestionResponseMappingStrategy();
    private final static OpenTextQuestionResponseDtoMappingStrategy openTextQuestionResponseDtoMappingStrategy =
            new OpenTextQuestionResponseDtoMappingStrategy();
    private final static ClosedQuestionResponseDtoMappingStrategy closedQuestionResponseDtoMappingStrategy =
            new ClosedQuestionResponseDtoMappingStrategy();


    public static ResponseDtoMappingStrategy create(ElementResponseDto dto) {
        if (dto instanceof OpenQuestionResponseDto) {
            return openTextQuestionResponseDtoMappingStrategy;
        } else if (dto instanceof ClosedQuestionResponseDto) {
            return closedQuestionResponseDtoMappingStrategy;
        } else if (dto instanceof OpenNumericQuestionResponseDto) {
            return openNumericQuestionResponseMappingStrategy;
        }

        throw new RuntimeException("Invalid ElementResponseDto type");
    }
}
