package com.roal.survey_engine.domain.survey.dto.element;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OpenQuestionDto.class, name = "opq"),
        @JsonSubTypes.Type(value = ClosedQuestionDto.class, name = "clq"),
        @JsonSubTypes.Type(value = OpenNumericQuestionDto.class, name = "opnq"),
})
public interface AbstractElementDto {
    int position();

    long id();

    String type();
}