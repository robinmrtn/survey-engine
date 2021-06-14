package com.roal.survey_engine.dto.response;

import com.roal.survey_engine.entity.response.OpenTextQuestionResponse;

public class OpenQuestionResponseDto extends ElementResponseDto {

    private String value;

    public OpenQuestionResponseDto(long id, String value) {
        super(id);
        this.value = value;
    }

    public OpenQuestionResponseDto() {

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    OpenTextQuestionResponse mapToEntity() {
        var openTextQuestionResponse = new OpenTextQuestionResponse();
        // some mapping
        return openTextQuestionResponse;
    }
}
